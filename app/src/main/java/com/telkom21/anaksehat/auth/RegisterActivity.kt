package com.telkom21.anaksehat.auth

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.emailLayout.helperText = null
        binding.usernameLayout.helperText = null
        binding.passwordLayout.helperText = null
        binding.tipeAkunLayout.helperText = null
        binding.confirmPasswordLayout.helperText = null
        registUser()
        toLogin()
        accTypeDropdown()
        back()
        emailFocusListener()
        passwordFocusListener()
        usernameFocusListener()
    }

    private fun registUser() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        binding.registBtn.setOnClickListener {
            val email = binding.emailRegistField.text.toString()
            val username = binding.usernameRegistField.text.toString()
            val password = binding.passwordRegistField.text.toString()
            val confirmPassword = binding.confirmPasswordField.text.toString()
            val tipeAkun = binding.tipeAkundropdown.text.toString()
            val passwordError = validPassword()

            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && tipeAkun.isNotEmpty()) {
                if (passwordError == null) {
                    if (password == confirmPassword) {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val uid = user?.uid ?: ""

                                // Store username and other user data in the database
                                val userRef = database.child(uid)
                                userRef.child("username").setValue(username)
                                userRef.child("email").setValue(email)
                                userRef.child("tipeAkun").setValue(tipeAkun)
                                userRef.child("password").setValue(password)

                                Toast.makeText(this, "Berhasil mendaftarkan akun.", Toast.LENGTH_SHORT).show()

                                // Redirect based on account type
                                if (tipeAkun == "Petugas") {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                } else if (tipeAkun == "Pengguna") {
                                    startActivity(Intent(this, ChildRegistActivity::class.java))
                                }
                                finish() // Finish the current activity
                            } else {
                                try {
                                    throw task.exception!!
                                } catch (e: FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(this, "Email atau kata sandi tidak valid.", Toast.LENGTH_SHORT).show()
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    Toast.makeText(this, "Email ini sudah pernah didaftarkan.", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Kata sandi tidak sama.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Kata sandi minimal 8 karakter dan harus mengandung huruf besar, huruf kecil, angka, dan simbol.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Semua isian harus diisi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toLogin() {
        binding.textMasuk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun accTypeDropdown() {
        val dropdown = binding.tipeAkundropdown
        if (dropdown != null) {
            val tipeAkun = resources.getStringArray(R.array.acc_type)
            val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, tipeAkun)
            dropdown.setAdapter(arrayAdapter)
        }
    }

    private fun back() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun emailFocusListener() {
        binding.emailRegistField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.emailLayout.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailRegistField.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Email tidak valid"
        } else null
    }

    private fun usernameFocusListener() {
        binding.usernameRegistField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.usernameLayout.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String? {
        val usernameText = binding.usernameRegistField.text.toString()

        database.orderByChild("username").equalTo(usernameText)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        binding.usernameLayout.helperText = "Username sudah digunakan, coba yang lain."
                    } else {
                        binding.usernameLayout.helperText = null
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@RegisterActivity, "Error checking username: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })

        return null
    }

    private fun passwordFocusListener() {
        binding.passwordRegistField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.passwordLayout.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordRegistField.text.toString()
        return when {
            passwordText.length < 8 -> "Minimal 8 karakter"
            !passwordText.matches(".*[A-Z].*".toRegex()) -> "Harus mengandung huruf besar"
            !passwordText.matches(".*[a-z].*".toRegex()) -> "Harus mengandung huruf kecil"
            !passwordText.matches(".*[@#\$%^&+=].*".toRegex()) -> "Harus mengandung simbol"
            !passwordText.matches(".*[0-9].*".toRegex()) -> "Harus mengandung angka"
            else -> null
        }
    }
}
