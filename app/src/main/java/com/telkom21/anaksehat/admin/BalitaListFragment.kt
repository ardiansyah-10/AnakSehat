package com.telkom21.anaksehat.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.databinding.FragmentBalitaListBinding
import com.telkom21.anaksehat.utils.ChildDataClass
import com.telkom21.anaksehat.utils.ChildListAdapter


class BalitaListFragment : Fragment() {
    private lateinit var binding: FragmentBalitaListBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<ChildDataClass>
    private lateinit var adapter: ChildListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBalitaListBinding.inflate(inflater, container, false)
        userRecyclerView = binding.BalitaRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(this.context)
        userList = ArrayList()
        adapter = ChildListAdapter(userList)
        userRecyclerView.setHasFixedSize(true)
        binding.BalitaRecyclerView.adapter = adapter

        getUserData()

        return binding.root
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                for (userSnapshot in snapshot.children) {
                    val balitaSnapshot = userSnapshot.child("balita")
                    for (childSnapshot in balitaSnapshot.children) {
                        val child = childSnapshot.getValue(ChildDataClass::class.java)
                        if (child != null) {
                            userList.add(child)
                            userRecyclerView.adapter?.notifyItemInserted(userList.size - 1)
                        }
                    }
                }
                binding.BalitaRecyclerView.adapter = adapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                for (userSnapshot in snapshot.children) {
                    val balitaSnapshot = userSnapshot.child("balita")
                    for (childSnapshot in balitaSnapshot.children) {
                        val child = childSnapshot.getValue(ChildDataClass::class.java)
                        if (child != null) {
                            val index = userList.indexOfFirst { it.id == child.id }
                            if (index != -1) {
                                userList[index] = child
                                userRecyclerView.adapter?.notifyItemChanged(index)
                            }
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val balitaSnapshot = userSnapshot.child("balita")
                    for (childSnapshot in balitaSnapshot.children) {
                        val child = childSnapshot.getValue(ChildDataClass::class.java)
                        if (child != null) {
                            val index = userList.indexOfFirst { it.id == child.id }
                            if (index != -1) {
                                userList.removeAt(index)
                                userRecyclerView.adapter?.notifyItemRemoved(index)
                            }
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userList.clear()
                    for (userSnapshot in snapshot.children) {
                        val balitaSnapshot = userSnapshot.child("balita")
                        for (childSnapshot in balitaSnapshot.children) {
                            val child = childSnapshot.getValue(ChildDataClass::class.java)
                            if (child != null) {
                                child.parentId = userSnapshot.key ?: ""
                                userList.add(child)
                            }
                        }
                    }
                    val adapter = ChildListAdapter(userList)
                    userRecyclerView.adapter?.notifyDataSetChanged()
                    userRecyclerView.adapter = adapter
                    adapter.mListener = object : ChildListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val selectedUser = userList[position]
                            val intent = Intent(activity, ChildDetailActivity::class.java).apply {
                                putExtra("child_name", selectedUser.nama)
                                putExtra("parent_id", selectedUser.parentId)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}


//    private fun getUserData() {
//        dbref = FirebaseDatabase.getInstance().getReference("balita")//.child("balita")
//        dbref.addChildEventListener(object: ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val user = snapshot.getValue(ChildListDataClass::class.java)
//                if (user != null) {
//                    userList.add(user)
//                    userRecyclerView.adapter?.notifyItemInserted(userList.size - 1)
//                    binding.BalitaRecyclerView.adapter = adapter
//                }
//            }
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                val user = snapshot.getValue(ChildListDataClass::class.java)
//                if (user != null) {
//                    val index = userList.indexOfFirst { it.id == user.id }
//                    if (index != -1) {
//                        userList[index] = user
//                        userRecyclerView.adapter?.notifyItemChanged(index)
//                    }
//                }
//            }
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                val user = snapshot.getValue(ChildListDataClass::class.java)
//                if (user != null) {
//                    val index = userList.indexOfFirst { it.id == user.id }
//                    if (index != -1) {
//                        userList.removeAt(index)
//                        userRecyclerView.adapter?.notifyItemRemoved(index)
//                    }
//                }
//            }
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onCancelled(error: DatabaseError) {}
//        })
//
//        dbref.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    userList.clear()
//                    for (userSnapshot in snapshot.children) {
//                        val user = userSnapshot.getValue(ChildListDataClass::class.java)
//                        if (user != null) {
//                            userList.add(user)
//                        }
//                    }
//                    val adapter = ChildListAdapter(userList, "balita")
//                    userRecyclerView.adapter?.notifyDataSetChanged()
//                    userRecyclerView.adapter = adapter
//                    adapter.mListener = object : ChildListAdapter.onItemClickListener {
//                        override fun onItemClick(position: Int) {
//                            val selectedUser = userList[position]
//                            val intent = Intent(activity, ChildDetailActivity::class.java).apply {
//                                putExtra("balita_name", selectedUser.nama)
//                            }
//                            startActivity(intent)
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//    }
//}
