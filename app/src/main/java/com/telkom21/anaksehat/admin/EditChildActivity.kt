package com.telkom21.anaksehat.admin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.databinding.ActivityEditChildBinding
import com.telkom21.anaksehat.utils.ChildDataClass

class EditChildActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditChildBinding
    private lateinit var usersRef: DatabaseReference
    private var child: ChildDataClass? = null
    private var childName: String? = null
    private var parentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityEditChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        childName = intent.getStringExtra("child_name")
        parentId = intent.getStringExtra("parent_id")

        if (childName == null || parentId == null) {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        usersRef = FirebaseDatabase.getInstance().getReference("users").child(parentId!!)

        fetchChildData(childName!!, parentId!!)

        binding.cancelButton.setOnClickListener { finish() }
        binding.backBtn.setOnClickListener { finish() }

        binding.saveButton.setOnClickListener {
            val newNama = binding.childName.text.toString()
            val umur = binding.childAge.text.toString()
            val berat = binding.childWeight.text.toString()
            val tinggi = binding.childHeight.text.toString()

            if (newNama.isNotEmpty() && umur.isNotEmpty() && berat.isNotEmpty() && tinggi.isNotEmpty()) {
                updateChildData(newNama, umur, berat, tinggi)
            } else {
                Toast.makeText(this, "All fields must be valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChildData(childName: String, parentId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val badutaRef = database.child("users").child(parentId).child("baduta").orderByChild("nama").equalTo(childName)
        val balitaRef = database.child("users").child(parentId).child("balita").orderByChild("nama").equalTo(childName)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        child = childSnapshot.getValue(ChildDataClass::class.java)
                        if (child != null) {
                            child!!.parentId = parentId // Ensure parentId is set
                            populateFields(child!!)
                            return
                        }
                    }
                    Toast.makeText(this@EditChildActivity, "Child not found in database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditChildActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        badutaRef.addListenerForSingleValueEvent(listener)
        balitaRef.addListenerForSingleValueEvent(listener)
    }

    private fun populateFields(child: ChildDataClass) {
        binding.childName.setText(child.nama)
        binding.childAge.setText(child.umur)
        binding.childWeight.setText(child.berat)
        binding.childHeight.setText(child.tinggi)
    }

    private fun updateChildData(newNama: String, umur: String, berat: String, tinggi: String) {
        val updates = mapOf(
            "nama" to newNama,
            "umur" to umur,
            "berat" to berat,
            "tinggi" to tinggi
        )

        val database = FirebaseDatabase.getInstance().reference
        val badutaRef = database.child("users").child(parentId!!).child("baduta").orderByChild("nama").equalTo(child!!.nama)
        val balitaRef = database.child("users").child(parentId!!).child("balita").orderByChild("nama").equalTo(child!!.nama)

        val updateData = { childRef: DatabaseReference ->
            childRef.updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    child!!.nama = newNama
                    child!!.umur = umur
                    child!!.berat = berat
                    child!!.tinggi = tinggi
                    if (umur.toInt() > 24) {
                        moveChildData(childRef, child!!)
                    } else {
                        Toast.makeText(this, "Data berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Gagal memperbarui data. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        updateData(childSnapshot.ref)
                        return
                    }
                    Toast.makeText(this@EditChildActivity, "Data anak tidak ditemukan.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditChildActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        badutaRef.addListenerForSingleValueEvent(listener)
        balitaRef.addListenerForSingleValueEvent(listener)
    }

    private fun moveChildData(childRef: DatabaseReference, childData: ChildDataClass) {
        val balitaRef = FirebaseDatabase.getInstance().getReference("users").child(parentId!!).child("balita")
        val newChildKey = balitaRef.push().key

        if (newChildKey != null) {
            // Create a new child data object for "balita"
            val newChildData = mapOf(
                "nama" to childData.nama,
                "umur" to childData.umur,
                "berat" to childData.berat,
                "tinggi" to childData.tinggi,
                "jenisKelamin" to childData.jenisKelamin,
                "namaOrtu" to childData.namaOrtu,
                "history" to childData.history
            )

            // Update data in new location
            balitaRef.child(newChildKey).setValue(newChildData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Remove the child from baduta
                    childRef.removeValue().addOnCompleteListener { removeTask ->
                        if (removeTask.isSuccessful) {
                            Toast.makeText(this@EditChildActivity, "Data berhasil diperbarui ke 'balita'", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditChildActivity, "Data gagal dipindahkan dari 'baduta'", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@EditChildActivity, "Gagal memindahkan data ke 'balita'", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
