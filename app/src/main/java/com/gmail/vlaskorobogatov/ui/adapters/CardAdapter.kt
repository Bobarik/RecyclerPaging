package com.gmail.vlaskorobogatov.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.vlaskorobogatov.R
import com.gmail.vlaskorobogatov.databinding.BankCardLayoutBinding
import com.gmail.vlaskorobogatov.databinding.PreloaderLayoutBinding
import com.gmail.vlaskorobogatov.network.response.CardResponse
import com.gmail.vlaskorobogatov.ui.main.UiModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CardAdapter : PagingDataAdapter<
    UiModel,
    RecyclerView.ViewHolder>(
    CardsDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.preloader_layout -> {
                CardLoadViewHolder(PreloaderLayoutBinding.inflate(layoutInflater, parent, false))
            }
            else -> {
                CardViewHolder(
                    BankCardLayoutBinding.inflate(layoutInflater, parent, false),
                    parent.context
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            when (holder) {
                is CardViewHolder -> {
                    holder.bindCard((item as UiModel.Card).item)
                }
                is CardLoadViewHolder -> {
                    holder.itemView.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is UiModel.Card -> R.layout.bank_card_layout
        is UiModel.Footer -> R.layout.preloader_layout
        null -> throw IllegalStateException("Unknown view")
    }
}

class CardViewHolder(
    private val binding: BankCardLayoutBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bindCard(card: CardResponse) {
        binding.cardColors = CardColors.mapToBind(card.mobileAppDashboard)

        binding.cardTitle.text = card.mobileAppDashboard.companyName
        binding.cardScoreCount.text = card.customerMarkParameters.mark.toString()
        binding.cardLevelValue.text = card.customerMarkParameters.loyaltyLevel.name
        binding.cardCashbackValue.text = context.getString(
            R.string.cashback,
            card.customerMarkParameters.loyaltyLevel.number
        )
        Glide.with(context)
            .load(card.mobileAppDashboard.logo)
            .circleCrop()
            .into(binding.cardLogo)

        binding.cardButtonDetails.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Нажата кнопка \"Подробнее\".")
                .setMessage("Id компании: ${card.company.companyId}")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }

        binding.cardIconBin.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Нажата кнопка \"Удалить\".")
                .setMessage("Id компании: ${card.company.companyId}")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }

        binding.cardIconEye.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Нажата кнопка \"Глаз\".")
                .setMessage("Id компании: ${card.company.companyId}")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    }
}

private class CardsDiffCallback : DiffUtil.ItemCallback<UiModel>() {

    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return if (oldItem is UiModel.Card && newItem is UiModel.Card) {
            oldItem.item.company == newItem.item.company
        } else {
            oldItem::javaClass == newItem::javaClass
        }
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return oldItem == newItem
    }
}