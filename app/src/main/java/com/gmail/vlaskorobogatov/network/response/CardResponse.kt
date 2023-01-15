package com.gmail.vlaskorobogatov.network.response

data class CardResponse(
    val company: Company,
    val customerMarkParameters: CustomerMarkParameters,
    val mobileAppDashboard: MobileAppDashboard
) {
    data class Company(val companyId: String)
    data class CustomerMarkParameters(
        val loyaltyLevel: LoyaltyLevel,
        val mark: Int
    ) {
        data class LoyaltyLevel(
            val number: Int,
            val name: String,
            val requiredSum: Int,
            val markToCash: Int,
            val cashToMark: Int
        )
    }

    data class MobileAppDashboard(
        val companyName: String,
        val logo: String,
        val backgroundColor: String,
        val mainColor: String,
        val cardBackgroundColor: String,
        val textColor: String,
        val highlightTextColor: String,
        val accentColor: String
    )
}