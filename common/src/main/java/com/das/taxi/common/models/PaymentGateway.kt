package com.das.taxi.common.models

import com.squareup.moshi.Json

data class PaymentGateway(
    var id: Int,
    var title: String?,
    var type: PaymentGatewayType,
    var publicKey: String? = null,
    var privateKey: String? = null
)
enum class PaymentGatewayType (val rawValue: String) {
    @Json(name="stripe")
    Stripe("stripe"),
    @Json(name="braintree")
    Braintree("braintree"),
    @Json(name="flutterwave")
    Flutterwave("flutterwave"),
    @Json(name="paygate")
    PayGate("paygate");

    companion object {
        operator fun invoke(rawValue: String) = values().firstOrNull { it.rawValue == rawValue }
    }
}
