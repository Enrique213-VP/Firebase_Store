package com.firebaseauth.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.firebaseauth.core.Constants
import com.firebaseauth.models.User
import com.firebaseauth.ui.activities.LoginActivity
import com.firebaseauth.ui.activities.RegisterActivity
import com.firebaseauth.ui.activities.SettingsActivity
import com.firebaseauth.ui.activities.UserProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields.
            .document(userInfo.id)
            // Here the userInfo are field and the SetOption is set to merge.
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {

                // Here Call a function of base activity for transferring the result to it
                activity.userRegistrationSuccess()

            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName, "Error while registering the user.", e
                )

            }

    }

    fun getCurrentUserID(): String {
        //An instance of currentUser if it is not null or else it will be blank.
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the fields of user.
            .document(getCurrentUserID()).get().addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into
                // the user data model object
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.My_Shopp_Preferences, Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key: Value = LoggedInUsername : Sergio Vargas
                editor.putString(
                    Constants.LoggedInUsername, "${user.firstname} ${user.lastname}"
                )
                editor.apply()


                //Start
                when (activity) {
                    is LoginActivity -> {

                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)

                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }.addOnFailureListener { e ->

                // Hide the progress dialog if there is any error and print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName, "Error while getting user details", e
                )

            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error.
                        activity.userProfileUpdateSuccess()
                    }
                }
            }.addOnFailureListener {
                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error.
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.UserProfileImage + System.currentTimeMillis() + "." + Constants.getFileExtension(
                activity, imageFileUri
            )
        )

        sRef.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->

            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->

                    when (activity) {
                        is UserProfileActivity -> {
                            // Hide the progress dialog if there is any error.
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener {
                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error.
                        activity.hideProgressDialog()
                    }
                }

            }

    }

}