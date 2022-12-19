package com.firebaseauth.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.RED
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.core.Constants
import com.firebaseauth.databinding.ActivityUserProfileBinding
import com.firebaseauth.firestore.FireStoreClass
import com.firebaseauth.models.User
import com.firebaseauth.core.GlideLoader
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var mUserProfileDetail: User

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.hasExtra(Constants.ExtraUserDetails)) {
            //Get the user details from intents as a ParcelableExtra
            mUserProfileDetail = intent.getParcelableExtra(Constants.ExtraUserDetails)!!
        }

        binding.FirstName.isEnabled = false
        binding.FirstName.setText(mUserProfileDetail.firstname)

        binding.LastName.isEnabled = false
        binding.LastName.setText(mUserProfileDetail.lastname)

        binding.Email.isEnabled = false
        binding.Email.setText(mUserProfileDetail.email)

        binding.imageUser.setOnClickListener(this@UserProfileActivity)
        binding.buttonSubmit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.imageUser -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the Read_External_Storage permission and if it is not allowed
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChoose(this)
                    } else {

                        // Request permission
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.ReadStoragePermissionCode
                        )
                    }
                }

                R.id.buttonSubmit -> {

                    if (validateUserProfileDetail()) {

                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageFileUri != null)
                        FireStoreClass().uploadImageToCloudStorage(
                            this@UserProfileActivity,
                            mSelectedImageFileUri
                        )
                        else {
                            updateUserProfileDetail()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetail() {
        val userHashMap = HashMap<String, Any>()
        val mobileNumber = binding.PhoneNumber.text.toString().trim{ it <= ' '}
        val gender = if (binding.radioButtonMale.isChecked){
            Constants.Male
        } else {
            Constants.Female
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.Image] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.Mobile] = mobileNumber.toLong()
        }

        userHashMap[Constants.Gender] = gender

        userHashMap[Constants.CompleteProfile] = 1

        //showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().updateUserProfileData(this, userHashMap)

    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            "Your profile is updated successfully",
            Toast.LENGTH_LONG
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.ReadStoragePermissionCode) {
            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /* Check the Snack bar
                Snackbar.make(
                    binding.imageUser,
                    "The storage permission is granted",
                    BaseTransientBottomBar.LENGTH_LONG
                )
                    .setBackgroundTint(Color.rgb(0,128,0))
                    .show()
                */

                Constants.showImageChoose(this)
            } else {
                Snackbar.make(
                    binding.imageUser,
                    "You do not have permission, Denied",
                    BaseTransientBottomBar.LENGTH_LONG
                )
                    .setBackgroundTint(RED)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PickImageRequestCode) {
                if (data != null) {
                    try {
                        //The uri of selected image from phone storage
                        mSelectedImageFileUri = data.data!!

                        //binding.imageUser.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.imageUser
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            "Image selection failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection
            Log.e("CancelImage", "Cancelled")
        }
    }

    private fun validateUserProfileDetail(): Boolean {
        return when {
            TextUtils.isEmpty(binding.PhoneNumber.text.toString().trim { it <= ' ' }) -> {
                Snackbar.make(
                    binding.imageUser,
                    "Please enter mobile number",
                    BaseTransientBottomBar.LENGTH_LONG
                )
                    .setBackgroundTint(RED)
                    .show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        hideProgressDialog()

        mUserProfileImageURL = imageURL
        updateUserProfileDetail()
    }
}