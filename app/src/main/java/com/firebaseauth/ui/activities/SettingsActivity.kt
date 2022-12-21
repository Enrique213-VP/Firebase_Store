package com.firebaseauth.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.core.Constants
import com.firebaseauth.core.GlideLoader
import com.firebaseauth.databinding.ActivitySettingsBinding
import com.firebaseauth.firestore.FireStoreClass
import com.firebaseauth.models.User
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.textViewEdit.setOnClickListener(this)
        binding.buttonLogout.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSettings)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        binding.toolbarSettings.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: com.firebaseauth.models.User) {

        mUserDetails = user

        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.imageViewUserPhoto)
        binding.textViewName.text = "${user.firstname} ${user.lastname}"
        binding.textViewGender.text = user.gender
        binding.textViewEmail.text = user.email
        binding.textViewPhoneNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.textViewEdit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.ExtraUserDetails, mUserDetails)
                    startActivity(intent)
                }
                R.id.buttonLogout -> {
                    FirebaseAuth.getInstance().signOut()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}