package com.telkom21.anaksehat.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telkom21.anaksehat.R

class HistoryAdapter(
    private var historyList: MutableList<HistoryDataClass>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.history_item_layout, parent, false)
        return ViewHolder(itemView, mListener, context)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.tanggal.text = currentItem.tanggal
        holder.berat.text = "${currentItem.berat} kg"
        holder.tinggi.text = "${currentItem.tinggi} cm"
        // Additional fields binding as needed
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val tanggal: TextView = itemView.findViewById(R.id.tanggal)
        val berat: TextView = itemView.findViewById(R.id.berat)
        val tinggi: TextView = itemView.findViewById(R.id.tinggi)
        // Additional fields as needed

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
