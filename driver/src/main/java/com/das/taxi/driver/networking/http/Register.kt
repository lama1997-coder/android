package com.das.taxi.driver.networking.http

import com.das.taxi.common.models.Driver
import com.das.taxi.common.networking.http.interfaces.HTTPRequest
import com.das.taxi.common.utils.Adapters

class Register(jwtToken: String, driver: Driver) : HTTPRequest() {
    override val path: String = "driver/register"

    init {
        val mapped = Adapters.moshi.adapter<Driver>(Driver::class.java).toJsonValue(driver)!!
        this.params = mapOf("token" to jwtToken, "driver" to mapped)
    }
}