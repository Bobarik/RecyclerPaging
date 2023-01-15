package com.gmail.vlaskorobogatov.ui.adapters

import com.gmail.vlaskorobogatov.network.response.CardResponse

data class CardColors(
    val backgroundColor: String,
    val mainColor: String,
    val cardBackgroundColor: String,
    val textColor: String,
    val highlightTextColor: String,
    val accentColor: String
) {
    companion object {

        fun mapToBind(dashboard: CardResponse.MobileAppDashboard): CardColors {
            return CardColors(
                backgroundColor = dashboard.backgroundColor,
                mainColor = dashboard.mainColor,
                cardBackgroundColor = dashboard.cardBackgroundColor,
                textColor = dashboard.textColor,
                highlightTextColor = dashboard.highlightTextColor,
                accentColor = dashboard.accentColor,
            )
        }
    }
}