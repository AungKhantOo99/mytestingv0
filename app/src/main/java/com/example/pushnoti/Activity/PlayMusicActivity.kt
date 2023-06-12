package com.example.pushnoti.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pushnoti.Adapter.MusicAdapter
import com.example.pushnoti.R
import com.example.pushnoti.databinding.ActivityMainBinding
import com.example.pushnoti.databinding.ActivityPlayMusicBinding
import com.example.pushnoti.model.Audio.Audiodata
import java.io.IOException


class PlayMusicActivity : AppCompatActivity() {
     lateinit var audioList: ArrayList<Audiodata>
    var AudioUri: Uri? = null
    var checkdata=""
    lateinit var musicAdapter: MusicAdapter
    private lateinit var binding: ActivityPlayMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        audioList=ArrayList<Audiodata>()

        loadAudio()
        Log.d("AudioData",audioList.size.toString())
        binding.musiclist.apply {
            layoutManager=LinearLayoutManager(this@PlayMusicActivity)
            musicAdapter=MusicAdapter(this@PlayMusicActivity,audioList)
            adapter=musicAdapter
        }

    }
    @Throws(IOException::class)
    private fun playAudio(audioIndex: Int) {
//        val mediaPlayer = MediaPlayer()
//        mediaPlayer.setDataSource(audioList[audioIndex].data)
//        mediaPlayer.prepare()
//        mediaPlayer.start()
    }
    @SuppressLint("Range")
    private fun loadAudio() {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = contentResolver.query(uri, null, selection, null, sortOrder)
        if (cursor != null&& cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                val data: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val album: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                audioList?.add(Audiodata(data, title, album, artist))

            }
        }
        cursor?.close()
    }

}