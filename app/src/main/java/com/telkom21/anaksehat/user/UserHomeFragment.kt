package com.telkom21.anaksehat.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.databinding.FragmentUserHomeBinding
import com.telkom21.anaksehat.utils.UserChildDataClass
import com.telkom21.anaksehat.utils.UserChildListAdapter

class UserHomeFragment : Fragment() {
    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserChildDataClass>
    private lateinit var adapter: UserChildListAdapter
    private lateinit var currentUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        userRecyclerView = binding.userChildList
        userRecyclerView.layoutManager = LinearLayoutManager(this.context)
        userList = ArrayList()
        adapter = UserChildListAdapter(userList)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.adapter = adapter
        // Retrieve the username from SharedPreferences
        getUserData()
        return binding.root
    }

    private fun getUserData() {
        val sharedPreferences = requireContext()
            .getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE)
        currentUsername = sharedPreferences
            .getString("current_username",
                "") ?: ""
        dbref = FirebaseDatabase.getInstance()
            .getReference("users")
        dbref.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot:
                                      DataSnapshot) {
                if (snapshot.exists()) {
                    userList.clear()
                    for (userSnapshot in
                    snapshot.children) {
                        val badutaSnapshot =
                            userSnapshot
                                .child("baduta")
                        for (childSnapshot in
                        badutaSnapshot.children) {
                            val child = childSnapshot
                                .getValue(UserChildDataClass
                                ::class.java)
                            if (child != null &&
                                child.namaOrtu ==
                                currentUsername) {
                                child.parentId =
                                    userSnapshot.key ?: ""
                                userList.add(child)
                            }
                        }

                        val balitaSnapshot =
                            userSnapshot
                                .child("balita")
                        for (childSnapshot in
                        balitaSnapshot.children) {
                            val child =
                                childSnapshot
                                    .getValue(UserChildDataClass
                                    ::class.java)
                            if (child != null &&
                                child.namaOrtu ==
                                currentUsername) {
                                child.parentId =
                                    userSnapshot.key ?: ""
                                userList.add(child)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    adapter.mListener = object :
                        UserChildListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val selectedUser = userList[position]
                            val intent =
                                Intent(activity,
                                    UserChildDetailActivity
                                    ::class.java).apply {
                                putExtra("child_name",
                                    selectedUser.nama)
                                putExtra("parent_id",
                                    selectedUser.parentId)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
