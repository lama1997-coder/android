package com.das.taxi.driver.networking.http

import com.das.taxi.common.models.Driver
import com.das.taxi.common.models.Service
import com.das.taxi.common.networking.http.interfaces.HTTPRequest
import com.squareup.moshi.JsonClass

class GetRegisterInfo(jwtToken: String): HTTPRequest() {
    override val path: String = "driver/get"
    init {
        this.params = mapOf("token" to jwtToken)
    }
}
@JsonClass(generateAdapter = true)
data class RegistrationInfo(
    val driver: Driver,
    val services: List<Service>
)