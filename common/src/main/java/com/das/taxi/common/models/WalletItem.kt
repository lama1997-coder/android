package com.das.taxi.common.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletItem(
    val id: Int,
    val amount: Double = 0.0,
    val currency: String? = null
)