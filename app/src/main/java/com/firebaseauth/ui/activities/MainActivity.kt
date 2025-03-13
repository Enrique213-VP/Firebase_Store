package com.firebaseauth.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebaseauth.core.Constants
import com.firebaseauth.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        val sharedPreferences =
            getSharedPreferences(Constants.My_Shopp_Preferences, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LoggedInUsername, "")!!

        //Email user ( For the moment)
        val email = sharedPreferences.getString(Constants.LoggedInUsernameT, "")!!


        binding.tvUserId.text = "User is $username"
        binding.tvUserEmail.text = "Email is $email"

        binding.buttonLogout.setOnClickListener{
            //Logout from app
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}