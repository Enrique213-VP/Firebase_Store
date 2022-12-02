package com.firebaseauth.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.firebaseauth.MainActivity
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            onBackPressed()
        }

        //Action for input the user register
        binding.buttonRegister.setOnClickListener {
            validateRegister()
        }
    }

    private fun validateRegister() {
        when {
            //Lambda for write the first name
            TextUtils.isEmpty(binding.FirstName.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter your first name",
                    Toast.LENGTH_LONG
                ).show()
            }
            //Lambda for write the last name
            TextUtils.isEmpty(
                binding.LastName.text.toString()
                    .trim() { it <= ' ' }) || binding.LastName.length() <= 1 -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter your last name",
                    Toast.LENGTH_LONG
                ).show()
            }
            //Lambda for write the email
            TextUtils.isEmpty(binding.registerEmail.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter your email",
                    Toast.LENGTH_LONG
                ).show()
            }

            TextUtils.isEmpty(
                binding.registerPassword.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter password",
                    Toast.LENGTH_LONG
                ).show()
            }

            TextUtils.isEmpty(
                binding.ConfirmPassword.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please confirm password",
                    Toast.LENGTH_LONG
                ).show()
            }

            binding.ConfirmPassword.text.toString()
                .trim() { it <= ' ' } != binding.registerPassword.text.toString()
                .trim() { it <= ' ' } -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "The password is not the same",
                    Toast.LENGTH_LONG
                ).show()
            }

            !binding.termCond.isChecked -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "You must accept terms and conditions",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                registerUser()
            }
        }
    }

    private fun registerUser(){

        showProgressDialog(resources.getString(R.string.please_wait))

        val email: String = binding.registerEmail.text.toString().trim() { it <= ' ' }
        val password: String =
            binding.registerPassword.text.toString().trim() { it <= ' ' }

        //Create an instance and create a register a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->

                    hideProgressDialog()

                    // If the registration is successfully done
                    if (task.isSuccessful) {

                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        Toast.makeText(
                            this@RegisterActivity,
                            "You are registered successfully",
                            Toast.LENGTH_LONG
                        ).show()

                        /*
                        * Here the new user registered is automatically signed-in and send
                        * him to main screen with user id and email
                        * */

                        val intent =
                            Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("userId", firebaseUser.uid)
                        intent.putExtra("emailId", email)
                        startActivity(intent)
                        finish()

                    } else {
                        //If the register is generated a mistake
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            )
    }
}