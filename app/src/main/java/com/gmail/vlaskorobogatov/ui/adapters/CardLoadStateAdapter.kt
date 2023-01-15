package com.gmail.vlaskorobogatov.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.vlaskorobogatov.R
import com.gmail.vlaskorobogatov.databinding.PreloaderLayoutBinding

class CardLoadStateAdapter() : LoadStateAdapter<CardLoadViewHolder>() {
    override fun onBindViewHolder(holder: CardLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CardLoadViewHolder {
        return CardLoadViewHolder.create(parent)
    }
}

class CardLoadViewHolder(private val binding: PreloaderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.root.isVisible = loadState is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup): CardLoadViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.preloader_layout, parent, false)
            val binding = PreloaderLayoutBinding.bind(view)
            return CardLoadViewHolder(binding)
        }
    }
}