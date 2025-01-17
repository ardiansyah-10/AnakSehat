package com.telkom21.anaksehat.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.auth.ChangePasswordActivity
import com.telkom21.anaksehat.databinding.FragmentProfilBinding
import com.telkom21.anaksehat.help.HelpActivity
import com.telkom21.anaksehat.info.InfoActivity
import com.telkom21.anaksehat.utils.FrontActivity

class profilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        fetchUsername()
        logOut()
        help()
        changePassword()
        about()
        return binding.root
    }

    private fun about() {
        binding.about.setOnClickListener{
            startActivity(Intent(context, InfoActivity::class.java))
        }
    }

    private fun logOut() {
        binding.logOut.setOnClickListener {
            auth.signOut()
            signOutUser()
        }
    }

    private fun help(){
        binding.bantuan.setOnClickListener {
            startActivity(Intent(context, HelpActivity::class.java))
        }
    }

    private fun changePassword() {
        binding.changePassword.setOnClickListener {
            startActivity(Intent(context, ChangePasswordActivity::class.java))
        }
    }

    private fun signOutUser() {
        val intent = Intent(requireActivity(), FrontActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(requireActivity(), "Berhasil Keluar", Toast.LENGTH_SHORT).show()
        requireActivity().finish()
    }

    private fun fetchUsername() {
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("users/$uid/username")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.value.toString()
                        binding.username.text = username
                    } else {
                        Toast.makeText(context, "-", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "-", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "belum login", Toast.LENGTH_SHORT).show()
        }
    }
}
