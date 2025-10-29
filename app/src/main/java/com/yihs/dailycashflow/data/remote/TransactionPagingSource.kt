package com.yihs.dailycashflow.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yihs.dailycashflow.data.model.Transaction
import okio.IOException

class TransactionPagingSource(
    private val apiService: ApiService,
    private val type: String,
    private val range: String
    ) : PagingSource<Int, Transaction>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        return try {
            val page = params.key ?: INIT_PAGE_INDEX
            val responseData = apiService.getTransactionPaging(type, range, page)

            //get data response
            val items = responseData.data
            val pagination = responseData.pagination

            val prefKey = if (page == INIT_PAGE_INDEX) null else page-1
            val nextKey = if (page < pagination.lastPage) page + 1 else null

            LoadResult.Page(
                data = items,
                prevKey = prefKey,
                nextKey = nextKey
            )
        }catch (e : IOException){
            LoadResult.Error(e)
        }catch (e: IOException){
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object{
        const val INIT_PAGE_INDEX = 1
    }


}
