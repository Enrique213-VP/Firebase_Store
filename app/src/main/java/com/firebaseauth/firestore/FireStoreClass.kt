package com.firebaseauth.firestore

import android.util.Log
import com.firebaseauth.models.User
import com.firebaseauth.ui.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name.
        mFireStore.collection("users")
        // Document ID for users fields.
            .document(userInfo.id)
        // Here the userInfo are field and the SetOption is set to merge.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here Call a function of base activity for transferring the result to it
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )

            }

    }
}