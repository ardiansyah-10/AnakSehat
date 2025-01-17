package com.telkom21.anaksehat.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.admin.EditChildActivity

class ChildListAdapter(
    private var userList: ArrayList<ChildDataClass>
) : RecyclerView.Adapter<ChildListAdapter.ViewHolderClass>() {

    private lateinit var context: Context
    private var filteredList: ArrayList<ChildDataClass> = ArrayList(userList)
    lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(itemView, mListener, context)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = filteredList[position]
        holder.nama.text = currentItem.getFirstName()
        holder.umur.text = currentItem.umur ?: ""
        holder.moreBtn.setOnClickListener { showPopupMenu(it, currentItem) }
        holder.itemView.setOnLongClickListener {
            showPopupMenu(it, currentItem)
            true
        }
    }

    private fun showPopupMenu(view: View, child: ChildDataClass) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.more_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem -> handleMenuItemClick(menuItem, child) }
        popup.show()
    }

    private fun handleMenuItemClick(item: MenuItem, child: ChildDataClass): Boolean {
        return when (item.itemId) {
            R.id.editData -> {
                showEditActivity(child)
                true
            }
            R.id.deleteData -> {
                deleteChildData(child)
                true
            }
            else -> false
        }
    }

    private fun deleteChildData(child: ChildDataClass) {
        val database = FirebaseDatabase.getInstance().reference
        val badutaRef = database.child("users").child(child.parentId).child("baduta").orderByChild("nama").equalTo(child.nama)
        val balitaRef = database.child("users").child(child.parentId).child("balita").orderByChild("nama").equalTo(child.nama)

        val deleteListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        childSnapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show()
                                // Update the adapter dataset after successful deletion
                                val index = filteredList.indexOf(child)
                                if (index != -1) {
                                    filteredList.removeAt(index)
                                    notifyItemRemoved(index)
                                }
                            } else {
                                Toast.makeText(context, "Failed to delete data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Child not found in database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Check in both "baduta" and "balita"
        badutaRef.addListenerForSingleValueEvent(deleteListener)
        balitaRef.addListenerForSingleValueEvent(deleteListener)
    }

    private fun showEditActivity(child: ChildDataClass) {
        val intent = Intent(context, EditChildActivity::class.java).apply {
            putExtra("child_name", child.nama)
            putExtra("parent_id", child.parentId)
        }
        context.startActivity(intent)
    }

    class ViewHolderClass(itemView: View, listener: onItemClickListener, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.namaAnak)
        val umur: TextView = itemView.findViewById(R.id.umurAnak)
        val moreBtn: ImageView = itemView.findViewById(R.id.moreBtn)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}