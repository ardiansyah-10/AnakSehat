package com.telkom21.anaksehat.auth

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.telkom21.anaksehat.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        resetPassword()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun resetPassword(){
        binding.changePasswordButton.setOnClickListener {
            val currentUser = auth.currentUser
            val currentPassword = binding.currentPasswordField.text.toString()
            val newPassword = binding.newPasswordField.text.toString()
            val confirmPassword = binding.confirmPasswordField.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Kata sandi tidak sama.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentUser != null) {
                val credential = EmailAuthProvider
                    .getCredential(currentUser
                        .email!!,
                        currentPassword)
                currentUser.reauthenticate(credential)
                    .addOnCompleteListener {
                        reauthTask -> if (
                        reauthTask.isSuccessful) {
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                            val password = binding
                                .newPasswordField
                                .text.toString()
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this,
                                    "Kata sandi berhasil diubah",
                                    Toast.LENGTH_SHORT).show()
                                val user = auth.currentUser
                                val uid = user?.uid ?: ""

                                val userRef = database.child(uid)
                                userRef.child("password")
                                    .setValue(password)
                            } else {
                                Toast.makeText(this,
                                    "Gagal mengubah sandi",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this,
                            "Gagal autentikasi",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
