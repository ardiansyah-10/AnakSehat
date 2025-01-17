package com.telkom21.anaksehat.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.FragmentTimbangBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class timbangFragment : Fragment() {

    private lateinit var binding: FragmentTimbangBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimbangBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")
        fetchChildrenNames()
        fetchData()
        setupSaveButton()
        return binding.root
    }

    private fun fetchChildrenNames() {
        val childrenNames = mutableSetOf<String>()
        usersRef.
        addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot:
                                      DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in
                    snapshot.children) {
                        val balitaSnapshot =
                            userSnapshot
                                .child("balita")
                        for (childSnapshot in
                        balitaSnapshot.children) {
                            val name =
                                childSnapshot
                                .child("nama")
                                .getValue(String::class.java)
                            name?.let { childrenNames.add(it) }
                        }
                        val badutaSnapshot =
                            userSnapshot
                                .child("baduta")
                        for (childSnapshot in
                        badutaSnapshot.children) {
                            val name =
                                childSnapshot
                                    .child("nama")
                                    .getValue(String::class.java)
                            name?.let { childrenNames.add(it) }
                        }
                    }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        R.id.dropdownText,
                        childrenNames.toList()
                    )
                    binding.childDropdown.setAdapter(adapter)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchData() {
        val sensorValRef = database
            .getReference("sensorVal")
        sensorValRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot:
                                      DataSnapshot) {
                if (snapshot.exists()) {
                    val beratValues =
                        mutableListOf<Float>()
                    val tinggiValues =
                        mutableListOf<Int>()

                    for (sensorSnapshot in snapshot.children) {
                        val berat = sensorSnapshot
                            .child("berat")
                            .getValue(Float::class.java)
                        val tinggi = sensorSnapshot
                            .child("tinggi")
                            .getValue(Int::class.java)
                        berat?.let { beratValues.add(it) }
                        tinggi?.let { tinggiValues.add(it) }
                    }

                    if (beratValues.isNotEmpty()) {
                        binding.beratBadan
                            .setText(beratValues
                                .joinToString(", "))
                    }

                    if (tinggiValues.isNotEmpty()) {
                        binding.tinggiBadan
                            .setText(tinggiValues
                                .joinToString(", "))
                    }
                } else {
                    Toast.makeText(requireContext(),
                        "No sensor data available",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),
                    "Failed to load sensor data",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSaveButton() {
        binding.simpanData.setOnClickListener {
            val selectedChild =
                binding.childDropdown
                    .text.toString()
            val beratBadan =
                binding.beratBadan
                    .text.toString()
            val tinggiBadan =
                binding.tinggiBadan
                    .text.toString()

            if (selectedChild.isNotEmpty()
                && beratBadan.isNotEmpty()
                && tinggiBadan.isNotEmpty()) {
                saveDataToChildNode(
                    selectedChild,
                    beratBadan,
                    tinggiBadan)
            } else {
                Toast.makeText(requireContext(),
                    "All fields must be filled",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDataToChildNode(
        childName: String,
        berat: String,
        tinggi: String) {
        val currentTimestamp =
            SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault())
                .format(Date())
        val beratString = berat
        val tinggiString = tinggi

        val saveData = { childRef:
                         DatabaseReference ->
            val uniqueId = childRef.push().key
            if (uniqueId != null) {
                val updates = mapOf(
                    "berat" to beratString,
                    "tinggi" to tinggiString,
                    "history/$uniqueId/berat"
                            to beratString,
                    "history/$uniqueId/tinggi"
                            to tinggiString,
                    "history/$uniqueId/tanggal"
                            to currentTimestamp
                )

                childRef.updateChildren(updates)
                    .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Berhasil menyimpan data",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(),
                            "Gagal menyimpan data",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Check in users node
        usersRef.
        addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot:
                                      DataSnapshot) {
                var childFound = false
                if (snapshot.exists()) {
                    for (userSnapshot in
                    snapshot.children) {
                        val balitaSnapshot =
                            userSnapshot.child("balita")
                        for (childSnapshot in
                        balitaSnapshot.children) {
                            val name = childSnapshot
                                .child("nama")
                                .getValue(String::class.java)
                            if (name == childName) {
                                saveData(childSnapshot.ref)
                                childFound = true
                                break
                            }
                        }
                        if (!childFound) {
                            val badutaSnapshot =
                                userSnapshot
                                    .child("baduta")
                            for (childSnapshot in
                            badutaSnapshot.children) {
                                val name =
                                    childSnapshot
                                        .child("nama")
                                        .getValue(String::class.java)
                                if (name == childName) {
                                    saveData(childSnapshot.ref)
                                    childFound = true
                                    break
                                }
                            }
                        }
                        if (childFound) break
                    }
                    if (!childFound) {
                        Toast.makeText(
                            requireContext(),
                            "Nama anak tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Database error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
