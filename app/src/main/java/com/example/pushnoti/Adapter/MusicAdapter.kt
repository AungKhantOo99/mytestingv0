package com.example.pushnoti.Adapter

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.pushnoti.R
import com.example.pushnoti.model.Audio.Audiodata


 var mediaPlayer =MediaPlayer()
var state=0
class MusicAdapter(val context: Context,val data:ArrayList<Audiodata>) : RecyclerView.Adapter<MusicAdapter.viewHolder>(){

    class viewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.viewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.music_item,parent,false)
        return MusicAdapter.viewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicAdapter.viewHolder, position: Int) {
       val currentitem=data[position]
       val image=holder.itemView.findViewById<ImageView>(R.id.play)
       val title=holder.itemView.findViewById<TextView>(R.id.title)
       title.setText(currentitem.title)
        title.setSelected(true);
        val imageResId = if (currentitem.isSelect) R.drawable.pause else R.drawable.play
        image.setImageResource(imageResId)
       image.setOnClickListener {
           for (item in data){
               item.isSelect=false
           }
           currentitem.isSelect = true
           notifyDataSetChanged()
        if(state==1) {
            Log.d("checkplayplus", state.toString())
            mediaPlayer.stop()
            mediaPlayer.reset()
            state=0
        }
            Log.d("checkplayplus", state.toString())
            mediaPlayer.setDataSource(data[position].data)
            mediaPlayer.prepare()
            mediaPlayer.start()
            state=1

       }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}