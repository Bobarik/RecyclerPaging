package com.gmail.vlaskorobogatov.network.requests

import com.google.gson.annotations.SerializedName

data class CardRequest(
    @SerializedName("offset")
    val offset: Int
)