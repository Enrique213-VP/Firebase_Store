package com.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebaseauth.databinding.ActivityMainBinding
import com.firebaseauth.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")
        val emailId = intent.getStringExtra("emailId")

        binding.tvUserId.text = "User ID: $userId"
        binding.tvUserEmail.text = "Email user: $emailId"

        binding.buttonLogout.setOnClickListener{
            //Logout from app
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}