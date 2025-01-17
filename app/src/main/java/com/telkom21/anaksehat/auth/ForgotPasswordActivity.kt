package com.telkom21.anaksehat.auth

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        binding.forgotPasswordButton.setOnClickListener {
            val email = binding.emailField.text.toString().trim()
            val newPassword = binding.newPasswordField.text.toString().trim()
            val confirmPassword = binding.confirmPasswordField.text.toString().trim()

            if (validateInputs(email, newPassword, confirmPassword)) {
                fetchCurrentPasswordAndReset(email, newPassword)
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(email: String, newPassword: String, confirmPassword: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPassword.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun fetchCurrentPasswordAndReset(email: String, newPassword: String) {
        val emailRef = database.orderByChild("email").equalTo(email)
        emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val currentPassword = userSnapshot.child("password").getValue(String::class.java)
                        if (currentPassword != null) {
                            reauthenticateAndChangePassword(email, currentPassword, newPassword)
                        } else {
                            Toast.makeText(this@ForgotPasswordActivity, "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "email tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ForgotPasswordActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reauthenticateAndChangePassword(email: String, currentPassword: String, newPassword: String) {
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.email == email) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Kata sandi berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            val uid = currentUser.uid
                            val userRef = database.child(uid)
                            userRef.child("password").setValue(newPassword)
                        } else {
                            Toast.makeText(this, "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Autentikasi gagal", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Pengguna tidak ditemukan atau email tidak cocok", Toast.LENGTH_SHORT).show()
        }
    }
}
