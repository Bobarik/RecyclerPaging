package com.gmail.vlaskorobogatov.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.vlaskorobogatov.databinding.FragmentMainBinding
import com.gmail.vlaskorobogatov.network.exceptions.BadRequestException
import com.gmail.vlaskorobogatov.network.exceptions.InternalServerException
import com.gmail.vlaskorobogatov.network.exceptions.UnauthorizedException
import com.gmail.vlaskorobogatov.ui.adapters.CardAdapter
import com.gmail.vlaskorobogatov.ui.adapters.CardLoadStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecyclerFragment : Fragment() {

    private val viewModel: RecyclerViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cards = viewModel.cards
        bindState(binding, cards)
    }

    private fun bindState(
        binding: FragmentMainBinding,
        pagingData: Flow<PagingData<UiModel>>
    ) {
        val cardAdapter = CardAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = cardAdapter.withLoadStateAdapters(
            requireContext(),
            footer = CardLoadStateAdapter(),
            header = CardLoadStateAdapter()
        )
        bindList(cardAdapter = cardAdapter, pagingData = pagingData)
    }

    private fun bindList(
        cardAdapter: CardAdapter,
        pagingData: Flow<PagingData<UiModel>>,
    ) {
        lifecycleScope.launch {
            pagingData.collectLatest() {
                cardAdapter.submitData(it)
            }
        }
    }
}

fun <T : Any, V : RecyclerView.ViewHolder> PagingDataAdapter<T, V>.withLoadStateAdapters(
    context: Context,
    header: LoadStateAdapter<*>,
    footer: LoadStateAdapter<*>
): ConcatAdapter {
    addLoadStateListener { loadStates ->

        header.loadState = loadStates.refresh
        footer.loadState = loadStates.append

        val errorState = loadStates.source.refresh as? LoadState.Error
            ?: loadStates.refresh as? LoadState.Error
            ?: loadStates.source.append as? LoadState.Error
            ?: loadStates.source.prepend as? LoadState.Error
            ?: loadStates.append as? LoadState.Error
            ?: loadStates.prepend as? LoadState.Error
        errorState?.let { error ->

            val errorText = when (error.error) {
                is InternalServerException -> {
                    "Все упало"
                }
                is BadRequestException -> {
                    error.error.message
                }
                is UnauthorizedException -> {
                    "Ошибка авторизации"
                }
                else -> {
                    "Все упало, нашей стороны!"
                }
            }

            MaterialAlertDialogBuilder(context).setTitle(errorText)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }
    }

    return ConcatAdapter(header, this, footer)
}
