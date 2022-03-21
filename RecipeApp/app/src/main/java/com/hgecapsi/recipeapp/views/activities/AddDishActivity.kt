package com.hgecapsi.recipeapp.views.activities

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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


class AddDishActivity : AppCompatActivity() {

    private lateinit var addDishBinding: ActivityAddDishBinding //time to start differentiating binding variables

    //global variable for img selection layout "pick_dish_img"
    private lateinit var pickImgBinding: PickDishImgBinding

    //declare dialog box global so you can dismiss it from everywhere in this activity
    private lateinit var dialog: Dialog

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
    }
}