package com.gmail.vlaskorobogatov.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertFooterItem
import androidx.paging.map
import com.gmail.vlaskorobogatov.network.repository.LoadCardRepository
import com.gmail.vlaskorobogatov.network.response.CardResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RecyclerViewModel @Inject constructor(
    private val repository: LoadCardRepository
) : ViewModel() {

    val cards: Flow<PagingData<UiModel>> = Pager(
        config = PagingConfig(
            pageSize = 5,
            enablePlaceholders = true,
            initialLoadSize = 4
        ),
        pagingSourceFactory = { repository.cardsPagingSource() }
    ).flow.map { pagingData ->
        pagingData.map {
            UiModel.Card(it) as UiModel
        }
    }.map { pagingData ->
        pagingData.insertFooterItem(item = UiModel.Footer)
    }.cachedIn(viewModelScope)
}

sealed class UiModel {

    class Card(val item: CardResponse) : UiModel()

    object Footer : UiModel()
}