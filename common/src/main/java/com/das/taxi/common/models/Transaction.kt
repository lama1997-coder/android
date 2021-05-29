package com.das.taxi.common.models

import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class Transaction(
    var amount: Double,
    val currency: String,
    var documentNumber: String?,
    var transactionTime: Long,
    var details: String?,
    var id: Long,
    var transactionType: String
) {
    val day: String
        get() {
            val date = Date(transactionTime)
            return SimpleDateFormat("dd", Locale.getDefault()).format(date)
        }
    val month: String
        get() {
            //val months = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
            val date = Date(transactionTime)
            return SimpleDateFormat("MMM", Locale.getDefault()).format(date)
        }
}