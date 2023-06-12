package com.example.pushnoti.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pushnoti.Adapter.MyDataAdapter
import com.example.pushnoti.databinding.ActivityPaginationBinding
import com.example.pushnoti.pagination.MyPagingSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class PaginationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaginationBinding
    private var myadapter= MyDataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pagiantion.layoutManager=LinearLayoutManager(this)
        binding.pagiantion.adapter=myadapter
        val pagingSource = MyPagingSource() // Create an instance of your PagingSource
        val pagerConfig = PagingConfig(5)

        val pager = Pager(pagerConfig) {
            pagingSource
        }

        val pagingDataFlow: Flow<PagingData<com.example.pushnoti.model.response.Result>> = pager.flow.cachedIn(lifecycleScope)
        lifecycleScope.launch {
                pagingDataFlow.collectLatest { pagingData ->
                myadapter.submitData(pagingData)
                binding.loading.visibility=View.GONE
                Log.d("Showdata",pagingData.toString())
            }
        }
    }

}