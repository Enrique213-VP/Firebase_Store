package com.firebaseauth.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.firebaseauth.R
import com.firebaseauth.core.BaseActivity
import com.firebaseauth.databinding.ActivityRegisterBinding
import com.firebaseauth.firestore.FireStoreClass
import com.firebaseauth.models.User
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

    private fun registerUser() {

        showProgressDialog(resources.getString(R.string.please_wait))

        val email: String = binding.registerEmail.text.toString().trim() { it <= ' ' }
        val password: String =
            binding.registerPassword.text.toString().trim() { it <= ' ' }

        //Create an instance and create a register a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->

                    // If the registration is successfully done
                    if (task.isSuccessful) {

                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            binding.FirstName.text.toString().trim { it <= ' ' },
                            binding.LastName.text.toString().trim { it <= ' ' },
                            binding.registerEmail.text.toString().trim { it <= ' ' }
                        )

                        FireStoreClass().registerUser(this@RegisterActivity, user)

                    } else {

                        hideProgressDialog()

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

    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            "You are registered successfully",
            Toast.LENGTH_LONG
        ).show()
    }
}