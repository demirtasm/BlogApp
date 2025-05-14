package com.madkit.blogapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.madkit.blogapp.databinding.ActivitySignInAndRegistrationBinding
import com.madkit.blogapp.register.WelcomeActivity

class SignInAndRegistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignInAndRegistrationBinding by lazy {
        ActivitySignInAndRegistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val action = intent.getStringExtra("action")
        if (action == "login") {
            binding.edtemail.visibility = View.VISIBLE
            binding.edtPassword.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.VISIBLE
            binding.tvNewHere.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE
            binding.edtname.visibility = View.GONE
            binding.cardView.visibility = View.GONE
            binding.btnLogin.setOnClickListener {
                val loginEmail = binding.edtemail.text.toString()
                val loginPassword = binding.edtPassword.text.toString()
                if(loginEmail.isEmpty() || loginPassword.isEmpty()){
                    Toast.makeText(this, "Please fill all the detail", Toast.LENGTH_SHORT).show()
                }else{
                    auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener {login->
                        if(login.isSuccessful){
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this, "Please enter correct details", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else if (action == "register") {
            binding.btnLogin.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE

            binding.btnRegister.setOnClickListener {
                val name = binding.edtname.text.toString()
                val email = binding.edtemail.text.toString()
                val password = binding.edtPassword.text.toString()
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all the detail", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { register ->
                            if (register.isSuccessful) {
                                val user = auth.currentUser
                                auth.signOut()
                                user?.let {
                                    val userReference = database.getReference("users")
                                    val userId = user.uid
                                    val userData = UserData(name, email)
                                    userReference.child(userId).setValue(userData)

                                    val storageReferance =
                                        storage.reference.child("profile_image/$userId.jpg")
                                    storageReferance.putFile(imageUri!!)
                                    startActivity(Intent(this, WelcomeActivity::class.java))
                                    finish()
                                }
                            }
                        }
                }
            }
        }
        binding.cardView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select image"),
                PICK_IMAGE_REQUEST
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform())
                .into(binding.imgRegister)
        }
    }
}