package com.telkom21.anaksehat.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telkom21.anaksehat.R

class UserChildListAdapter(private val userList: ArrayList<UserChildDataClass>) :
    RecyclerView.Adapter<UserChildListAdapter.UserChildViewHolder>() {

    lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChildViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
        return UserChildViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: UserChildViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.namaAnak.text = currentItem.nama
        holder.umurAnak.text = currentItem.umur
        // Set other views accordingly
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserChildViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val namaAnak: TextView = itemView.findViewById(R.id.namaAnak)
        val umurAnak: TextView = itemView.findViewById(R.id.umurAnak)
        // Initialize other views

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
