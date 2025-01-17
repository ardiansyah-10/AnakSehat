package com.telkom21.anaksehat.admin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityAddChildBinding

class AddChildActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChildBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddChildBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("users")
        saveChildDataToParent()
        dropdownMenu()
        back()
        setContentView(binding.root)
    }

    private fun saveChildDataToParent() {
        binding.saveFab.setOnClickListener {
            val namaAnakEditText = binding.namaAnak
            val jenisKelaminDropdown = binding.jenisKelamindropdown
            val umurEditText = binding.umur
            val namaOrtuEditText = binding.namaOrtu
            val nama = namaAnakEditText.text.toString().trim()
            val umurString = umurEditText.text.toString().trim()
            val namaOrtu = namaOrtuEditText.text.toString().trim()

            if (umurString.toIntOrNull() != null) {  // Check if umur is a valid integer
                val jenisKelamin = jenisKelaminDropdown.text.toString().trim()

                val anakData = HashMap<String, Any>()
                anakData["nama"] = nama
                anakData["umur"] = umurString
                anakData["jenisKelamin"] = jenisKelamin
                anakData["namaOrtu"] = namaOrtu

                // Determine the child category based on umur
                val childPath = if (umurString.toInt() in 0..24) {
                    "baduta"
                } else if (umurString.toInt() in 25..60) {
                    "balita"
                } else {
                    Toast.makeText(this@AddChildActivity, "Umur yang dimasukkan tidak boleh lebih dari 60 bulan.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Find the parent ID based on namaOrtu (matching username)
                usersRef.orderByChild("username").equalTo(namaOrtu).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val parentId = userSnapshot.key
                                // Save to parent node
                                val parentChildRef = usersRef.child(parentId!!).child(childPath).push()
                                parentChildRef.setValue(anakData)
                                    .addOnSuccessListener {
                                        namaAnakEditText.text?.clear()
                                        umurEditText.text?.clear()
                                        jenisKelaminDropdown.setText("")
                                        namaOrtuEditText.text?.clear()
                                        Toast.makeText(this@AddChildActivity,
                                            "Data anak berhasil ditambahkan.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                    }
                            }
                        } else {}
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            } else {}
        }
    }

    private fun dropdownMenu() {
        val dropdown = binding.jenisKelamindropdown
        if (dropdown != null) {
            val jenisKelamin = resources.getStringArray(R.array.gender_options)
            val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, jenisKelamin)
            dropdown.setAdapter(arrayAdapter)
        } else {
            // Handle the case where the dropdown is null (optional)
        }
    }

    private fun back() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
