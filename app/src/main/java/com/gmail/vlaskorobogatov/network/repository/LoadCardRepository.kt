package com.gmail.vlaskorobogatov.network.repository

import com.gmail.vlaskorobogatov.di.qualifiers.DefaultNetworkApi
import com.gmail.vlaskorobogatov.network.pagingsource.CardPagingSource
import com.gmail.vlaskorobogatov.network.services.LoadDataService
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoadCardRepository @Inject constructor(
    @DefaultNetworkApi val loadDataService: LoadDataService,
    @DefaultNetworkApi val coroutineContext: CoroutineContext
) {

    fun cardsPagingSource() = CardPagingSource(loadDataService, coroutineContext)
}