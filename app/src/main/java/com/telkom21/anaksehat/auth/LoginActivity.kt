package com.telkom21.anaksehat.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.admin.MainActivity
import com.telkom21.anaksehat.databinding.ActivityLoginBinding
import com.telkom21.anaksehat.user.UserMainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
                   .getReference("users")
        sharedPreferences = getSharedPreferences("MyPrefs",
                            Context.MODE_PRIVATE)
        loginUser()
        toRegist()
        forgotPassword()
        back()
    }

    private fun loginUser() {
        binding.loginBtn.setOnClickListener {
            val username = binding.usernameLoginField
                           .text.toString()
            val password = binding.passwordLoginField
                           .text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                database.orderByChild("username")
                    .equalTo(username)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot:
                                                  DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val userSnapshot = dataSnapshot.children
                                                   .iterator().next()
                                val email = userSnapshot
                                    .child("email")
                                    .getValue(String::class.java) ?: ""
                                val tipeAkun = userSnapshot
                                    .child("tipeAkun")
                                    .getValue(String::class.java) ?: ""

                                if (email.isNotEmpty()) {
                                    auth.signInWithEmailAndPassword(
                                        email,
                                        password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Save the username in SharedPreferences
                                                val editor = sharedPreferences.edit()
                                                editor.putString(
                                                    "current_username",
                                                    username)
                                                editor.apply()

                                                val intent: Intent
                                                if (tipeAkun == "Petugas") {
                                                    intent = Intent(
                                                        this@LoginActivity,
                                                        MainActivity::class.java)
                                                } else if (tipeAkun == "Pengguna") {
                                                    intent = Intent(
                                                        this@LoginActivity,
                                                        UserMainActivity::class.java)
                                                } else {
                                                    Toast.makeText(
                                                        this@LoginActivity,
                                                        "Invalid user type",
                                                        Toast.LENGTH_SHORT).show()
                                                    return@addOnCompleteListener
                                                }
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    "Gagal masuk, cek nama pengguna atau password",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Email tidak ditemukan",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Pengguna tidak ditemukan",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Database error: ${error.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(
                    this,
                    "Username and password are required.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toRegist() {
        binding.textDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            //finish()
        }
    }

    private fun forgotPassword() {
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
           // finish()
        }
    }

    private fun back(){
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
