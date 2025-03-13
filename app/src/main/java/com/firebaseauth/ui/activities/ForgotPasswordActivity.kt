package com.firebaseauth.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonForgot.setOnClickListener {
            forgotPassWord()
        }

        binding.backToLoginText.setOnClickListener {
            onBackPressed()
        }
    }

    private fun forgotPassWord() {

        showProgressDialog()

        val email: String = binding.sendEmail.text.toString().trim() { it <= ' ' }

        if (email.isEmpty()) {
            Toast.makeText(
                this@ForgotPasswordActivity,
                "Please enter your email",
                Toast.LENGTH_LONG
            ).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Email sent successfully to reset your password, please, you must check your email address",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
        }
    }
}