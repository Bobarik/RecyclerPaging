package com.gmail.vlaskorobogatov.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmail.vlaskorobogatov.network.exceptions.UnexpectedHttpException
import com.gmail.vlaskorobogatov.network.requests.CardRequest
import com.gmail.vlaskorobogatov.network.response.CardResponse
import com.gmail.vlaskorobogatov.network.services.LoadDataService
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CardPagingSource constructor(
    private val loadDataService: LoadDataService,
    private val context: CoroutineContext
) : PagingSource<Int, CardResponse>() {

    companion object {
        private const val LOAD_SIZE = 5
    }

    private suspend fun getCards(cardRequest: CardRequest): Result<List<CardResponse>> {
        return withContext(context) {
            loadDataService.getAllCards(cardRequest)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardResponse> {
        val page = params.key ?: 0
        val offset = CardRequest(page)
        getCards(offset).onSuccess { list ->
            return LoadResult.Page(
                list,
                prevKey = if (page == 0) null else page - LOAD_SIZE,
                nextKey = if (list.isEmpty()) null else page + LOAD_SIZE
            )
        }.onFailure {
            return LoadResult.Error(it)
        }
        return LoadResult.Error(UnexpectedHttpException())
    }

    override fun getRefreshKey(state: PagingState<Int, CardResponse>): Int? {
        return state.anchorPosition
    }
}