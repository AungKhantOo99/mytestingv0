package com.example.pushnoti.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.pushnoti.R
import com.example.pushnoti.Adapter.RamdomAdapter
import com.example.pushnoti.ViewModel.RamdomDataViewModel
import com.example.pushnoti.model.response.Result

class RandomActivity : AppCompatActivity() {
    lateinit var randomviewmodel: RamdomDataViewModel
    lateinit var randomrfecyclerview:RecyclerView
    lateinit var animation:LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        supportActionBar?.hide()
        randomrfecyclerview=findViewById(R.id.recycler)
        animation=findViewById(R.id.animation_view)
        val layout=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
//        layout.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
//            override fun getSpanSize(position: Int): Int {
//                return if((position + 1) % 3 == 0) 2 else 1
//            }
//        }

        randomrfecyclerview.layoutManager=layout
        randomviewmodel= ViewModelProvider(this).get(RamdomDataViewModel::class.java)

        randomviewmodel.apiResponse.observe(this, Observer {
            animation.visibility= View.GONE
            Log.d("Check",it.toString())
            randomrfecyclerview.adapter= RamdomAdapter(this,it.results as ArrayList<Result>)
        })
    }

    override fun onStart() {
        super.onStart()
        randomviewmodel.startDataUpdates()
    }

    override fun onStop() {
        super.onStop()
        randomviewmodel.stopDataUpdates()
    }
}