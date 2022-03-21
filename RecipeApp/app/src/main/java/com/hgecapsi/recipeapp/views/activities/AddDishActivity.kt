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
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.databinding.ActivityAddDishBinding
import com.hgecapsi.recipeapp.databinding.PickDishImgBinding
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


class AddDishActivity : AppCompatActivity() {

    private lateinit var addDishBinding: ActivityAddDishBinding //time to start differentiating binding variables

    //global variable for img selection layout "pick_dish_img"
    private lateinit var pickImgBinding: PickDishImgBinding

    //declare dialog box global so you can dismiss it from everywhere in this activity
    private lateinit var dialog: Dialog

    // global variable fr storing img path
    private var _imgPath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addDishBinding = ActivityAddDishBinding.inflate(layoutInflater)
        setContentView(addDishBinding.root)

        pickImgBinding = PickDishImgBinding.inflate(layoutInflater)

        addDishBinding.ivAddDishImage.setOnClickListener{
            openCameraOrGalleryDialog()
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