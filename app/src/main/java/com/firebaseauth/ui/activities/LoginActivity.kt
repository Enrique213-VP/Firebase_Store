package com.firebaseauth.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.core.Constants
import com.firebaseauth.databinding.ActivityLoginBinding
import com.firebaseauth.firestore.FireStoreClass
import com.firebaseauth.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Click event assigned to register button.
        binding.register.setOnClickListener(this)
        // Click event assigned to login button.
        binding.buttonLogin.setOnClickListener(this)
        // Click event assigned to forgot password button.
        binding.tvForgotPassword.setOnClickListener(this)
    }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        if(user.profileCompleted == 0) {
            //If the user profile is incomplete
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.ExtraUserDetails, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

    // In login screen the clickable components are login button, forgot password text and register text
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.register -> {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
                R.id.buttonLogin -> {
                    validateUser()
                }
                R.id.tvForgotPassword -> {
                    startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
                }
            }
        }
    }

    private fun validateUser() {
        when {

            //Lambda for write the email
            TextUtils.isEmpty(binding.inputEmail.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter your email",
                    Toast.LENGTH_LONG
                ).show()
            }

            TextUtils.isEmpty(
                binding.inputPassword.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter password",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                loginUser()
            }

        }
    }

    private fun loginUser() {

        //See the progressBar
        showProgressDialog(resources.getString(R.string.please_wait))

        val email: String = binding.inputEmail.text.toString().trim() { it <= ' ' }
        val password: String = binding.inputPassword.text.toString().trim() { it <= ' ' }


        //Log-in using firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(
                        this@LoginActivity,
                        "You are logged in successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    FireStoreClass().getUserDetails(this@LoginActivity)

                } else {

                    hideProgressDialog()
                    //If the register is generated a mistake
                    Toast.makeText(
                        this@LoginActivity,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
    }
}