package com.example.pushnoti.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pushnoti.Network.RetrofitClient
import com.example.pushnoti.model.response.Result

class MyPagingSource() : PagingSource<Int, com.example.pushnoti.model.response.Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.pushnoti.model.response.Result> {
        return try {
            val nextPage = params.key ?: 1
            val pageSize = params.loadSize
            val response = RetrofitClient.JsonApi.getdata(nextPage, 5)

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }
}
