package com.madkit.blogapp.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.madkit.blogapp.MainActivity
import com.madkit.blogapp.R
import com.madkit.blogapp.SignInAndRegistrationActivity
import com.madkit.blogapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, SignInAndRegistrationActivity::class.java)
            intent.putExtra("action", "login")
            startActivity(intent)
            finish()
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, SignInAndRegistrationActivity::class.java)
            intent.putExtra("action", "register")
            startActivity(intent)
            finish()
        }
    }
}