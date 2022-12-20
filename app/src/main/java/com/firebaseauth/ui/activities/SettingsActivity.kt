package com.firebaseauth.ui.activities

import android.os.Bundle
import android.view.View
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.core.GlideLoader
import com.firebaseauth.databinding.ActivitySettingsBinding
import com.firebaseauth.firestore.FireStoreClass

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
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
        TODO("Not yet implemented")
    }
}