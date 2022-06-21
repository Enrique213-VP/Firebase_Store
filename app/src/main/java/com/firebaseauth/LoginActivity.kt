package com.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.firebaseauth.databinding.ActivityLoginBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener{
            startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener{

            when{

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
                    val email: String = binding.inputEmail.text.toString().trim() { it <= ' ' }
                    val password: String = binding.inputPassword.text.toString().trim() { it <= ' ' }

                    //Log-in using firebase

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->

                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "userId",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                intent.putExtra("emailId", email)
                                startActivity(intent)
                                finish()
                            }else{

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
        }
    }
}