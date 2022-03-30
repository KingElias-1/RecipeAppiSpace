package com.hgecapsi.recipeapp.views.activities

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.databinding.ActivityAddDishBinding
import com.hgecapsi.recipeapp.databinding.DialogCustomListBinding
import com.hgecapsi.recipeapp.databinding.PickDishImgBinding
import com.hgecapsi.recipeapp.views.adapters.CustomListItemAdapter
import com.hgecapsi.recipeapp.views.application.FavDishApplication
import com.hgecapsi.recipeapp.views.models.data.RecipeData
import com.hgecapsi.recipeapp.views.utils.Constants
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var addDishBinding: ActivityAddDishBinding //time to start differentiating binding variables

    //global variable for img selection layout "pick_dish_img"
    private lateinit var pickImgBinding: PickDishImgBinding

    //declare dialog box global so you can dismiss it from everywhere in this activity
    private lateinit var dialog: Dialog

    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication.FavDishApplication).repository)
    }

    // TODO Step 3: Create a global variable for dish details that we will receive via intent.
    // START
    private var mRecipeDetails: RecipeData? = null
    // END

    // global variable fr storing img path
    private var _imgPath: String = ""

    private lateinit var mCustomListDialog: Dialog

    private lateinit var CustomDialogbinding: DialogCustomListBinding // dialog for custom list dropdown layout (dialog_custom_list.xml)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addDishBinding = ActivityAddDishBinding.inflate(layoutInflater)
        setContentView(addDishBinding.root)

        // get edit intent from allRecipeFragment
        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)) {
            mRecipeDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }
        // END

        // TODO Step 7: Set the existing dish details to the view to edit.
        // START
        mRecipeDetails?.let {
            if (it.id != 0) {
                _imgPath = it.image

                // Load the dish image in the ImageView.
                Glide.with(this@AddDishActivity)
                    .load(_imgPath)
                    .centerCrop()
                    .into(addDishBinding.ivDishImage)

                addDishBinding.etTitle.setText(it.title)
                addDishBinding.etType.setText(it.type)
                addDishBinding.etCategory.setText(it.category)
                addDishBinding.etIngredients.setText(it.ingredients)
                addDishBinding.etCookingTime.setText(it.cookingTime)
                addDishBinding.etDirectionToCook.setText(it.directionToCook)

                addDishBinding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }
        // END

        pickImgBinding = PickDishImgBinding.inflate(layoutInflater)

        addDishBinding.ivAddDishImage.setOnClickListener{
            openCameraOrGalleryDialog()
        }

        //onclick listeners made for clickables in the layout
        addDishBinding.etType.setOnClickListener(this@AddDishActivity)
        addDishBinding.etCategory.setOnClickListener(this@AddDishActivity)
        addDishBinding.etCookingTime.setOnClickListener(this@AddDishActivity)
        addDishBinding.btnAddDish.setOnClickListener(this@AddDishActivity)

    }

    //onclick function override to prevent large blocks of code for each on click listener
    override fun onClick(view: View) {
        when (view.id) {
            R.id.et_type -> {
                customItemsListDialog(
                    resources.getString(R.string.title_select_dish_type),
                    Constants.dishTypes(),
                    Constants.DISH_TYPE
                )
                return
            }
            R.id.et_category -> {
                customItemsListDialog(
                    resources.getString(R.string.lbl_category),
                    Constants.dishCategories(),
                    Constants.DISH_CATEGORY
                )
                return
            }
            R.id.et_cooking_time -> {
                customItemsListDialog(
                    resources.getString(R.string.lbl_cooking_time_in_minutes),
                    Constants.dishCookTime(),
                    Constants.DISH_COOKING_TIME
                )
                return
            }
            R.id.btn_add_dish -> {
                // Define the local variables and get the EditText values.
                // For Dish Image we have the global variable defined already.

                val title = addDishBinding.etTitle.text.toString().trim { it <= ' ' }//trim removes empty whitespace can also use trim()
                val type = addDishBinding.etType.text.toString().trim { it <= ' ' }
                val category = addDishBinding.etCategory.text.toString().trim { it <= ' ' }
                val ingredients = addDishBinding.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTimeInMinutes =
                    addDishBinding.etCookingTime.text.toString().trim { it <= ' ' }
                val cookingDirection =
                    addDishBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

                when {

                    TextUtils.isEmpty(_imgPath) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_select_dish_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_title),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(type) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_select_dish_type),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(category) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_select_dish_category),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(ingredients) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_ingredients),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(cookingTimeInMinutes) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_select_dish_cooking_time),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(cookingDirection) -> {
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else ->{
                        // TODO Step 8: Update the data and pass the details to
                        //  ViewModel to Insert or Update
                        // START

                        var dishID = 0
                        var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                        var favoriteDish = false

                        mRecipeDetails?.let {
                            if (it.id != 0) {
                                dishID = it.id
                                imageSource = it.imageSource
                                favoriteDish = it.favoriteDish
                            }
                        }

                        val favDishDetails: RecipeData = RecipeData(
                            _imgPath,
                            imageSource,
                            title,
                            type,
                            category,
                            ingredients,
                            cookingTimeInMinutes,
                            cookingDirection,
                            favoriteDish,
                            dishID
                        )

                        if(dishID == 0) {
                            mFavDishViewModel.insert(favDishDetails)

                            Toast.makeText(
                                this@AddDishActivity,
                                "You successfully added a dish to the database.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // You even print the log if Toast is not displayed on emulator
                            Log.e("Insertion", "Success")
                        }
                        else{
                            mFavDishViewModel.update(favDishDetails)

                            Toast.makeText(
                                this@AddDishActivity,
                                "You successfully updated your dish details.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // You even print the log if Toast is not displayed on emulator
                            Log.e("Updating", "Success")
                        }


                        finish()
                    }

                }

            }

        }

    }

    private fun customItemsListDialog(title: String,
                                      itemList: ArrayList<String>,
                                      selection: String) {
        mCustomListDialog = Dialog(this@AddDishActivity)
        CustomDialogbinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(CustomDialogbinding.root)

        CustomDialogbinding.tvTitle.text = title

        // Set the LayoutManager that this RecyclerView will use.
        CustomDialogbinding.rvList.layoutManager = LinearLayoutManager(this@AddDishActivity)

        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(this@AddDishActivity, itemList, selection)

        // adapter instance is set to the recyclerview to inflate the items.
        CustomDialogbinding.rvList.adapter = adapter

        //Start the dialog and display it on screen.
        mCustomListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {

            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                addDishBinding.etType.setText(item)
            }

            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                addDishBinding.etCategory.setText(item)
            }
            else -> {
                mCustomListDialog.dismiss()
                addDishBinding.etCookingTime.setText(item)
            }
        }
    }

    private fun openCameraOrGalleryDialog() {
        dialog = Dialog(this)
        dialog.setContentView(pickImgBinding.root) //need to add dialog. before setContentView otherwise it will cover the whole screen

        pickImgBinding.useCam.setOnClickListener{
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        report.let{
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //removed the if for all permissions granted
                            startActivityForResult(intent, CAMERA)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {

                    }
                }).check()
            dialog.dismiss()
        }

        pickImgBinding.useGallery.setOnClickListener{
            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        // Here after all the permission are granted launch the gallery to select and image.
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(this@AddDishActivity,"Grant gallery access permission to pick image", Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).check()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA){
            data.let{
                val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap

                //save path of the image captured from camera
                _imgPath = saveImageToInternalStorage(thumbnail)
                
                Glide
                    .with(this@AddDishActivity)
                    .load(thumbnail)
                    .centerCrop()
                    .into(addDishBinding.ivDishImage)

                addDishBinding.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(this@AddDishActivity, R.drawable.edit)
                )
            }
        } else if(requestCode == GALLERY){
            data.let {
                val selectedImg = data?.data

                //set selected img to image view wiv glide
                Glide.with(this@AddDishActivity)
                    .load(selectedImg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // log exception
                            Log.e("TAG", "Error loading image", e)
                            return false // important to return false so the error placeholder can be placed
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val bitmap: Bitmap = resource!!.toBitmap()

                            _imgPath = saveImageToInternalStorage(bitmap)
                            Log.i("ImagePath",_imgPath)
                            return false // important to return false so the error placeholder can be placed
                        }

                    })
                    .into(addDishBinding.ivDishImage)

                addDishBinding.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(this@AddDishActivity, R.drawable.edit)
                )
            }
        }


    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         File creation mode: the default mode, where the created file can only
         be accessed by the calling application (or all applications sharing the
         same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Mention a file name to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        //return file to storage?
        return file.absolutePath
    }


    /**
     * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
     */
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }



    companion object{
        const val CAMERA = 100

        const val GALLERY = 200

        const val IMAGE_DIRECTORY = "DishImage"
    }

}