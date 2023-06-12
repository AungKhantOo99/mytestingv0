package com.example.pushnoti.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pushnoti.R
import com.example.pushnoti.model.response.responsedata
import com.squareup.picasso.Picasso

class RamdomAdapter (var context: Context, var item: ArrayList<com.example.pushnoti.model.response.Result>):
    RecyclerView.Adapter<RamdomAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val binddata=item[position]
        holder.itemView.findViewById<TextView>(R.id.name).setText(binddata.email)
        holder.itemView.findViewById<TextView>(R.id.address).setText(binddata.phone)
        Picasso.get().load(binddata.picture.large).into(holder.itemView.findViewById<ImageView>(R.id.image))
    }
}