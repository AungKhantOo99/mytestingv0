package com.example.pushnoti.pagination

import android.renderscript.Sampler
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pushnoti.Network.RetrofitClient
import com.example.pushnoti.model.pageresponse.paginationdata
import com.example.pushnoti.model.response.Result
import retrofit2.*

class MyPagingSource() : PagingSource<Int, com.example.pushnoti.model.pageresponse.Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.pushnoti.model.pageresponse.Result> {
        return try {
            val nextPage = params.key ?: 1
            val response = RetrofitClient.JsonApi.getdata(nextPage, 5).awaitResponse()
            val prevKey = if (nextPage == 1) null else nextPage - 1
            val nextKey = nextPage + 1
            Log.d("Check_result",response.body().toString())
            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = prevKey.takeIf { nextPage > 1 },
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.d("Check_Error",e.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.pushnoti.model.pageresponse.Result>): Int? {
        TODO("Not yet implemented")
    }
}
