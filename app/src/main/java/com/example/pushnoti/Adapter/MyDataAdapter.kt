package com.example.pushnoti.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pushnoti.R
import com.example.pushnoti.model.pageresponse.paginationdata

class MyDataAdapter : PagingDataAdapter<com.example.pushnoti.model.pageresponse.Result, MyDataViewHolder>(MyDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pageitem, parent, false)
        return MyDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyDataViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.itemname).setText(item!!.email)

    }
}

class MyDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.itemname)


}

class MyDataDiffCallback : DiffUtil.ItemCallback<com.example.pushnoti.model.pageresponse.Result>() {
    override fun areItemsTheSame(oldItem: com.example.pushnoti.model.pageresponse.Result, newItem: com.example.pushnoti.model.pageresponse.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem:com.example.pushnoti.model.pageresponse.Result, newItem:com.example.pushnoti.model.pageresponse.Result): Boolean {
        return oldItem == newItem
    }
}
