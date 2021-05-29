package com.das.taxi.driver.networking.http

import android.util.Log
import com.das.taxi.common.models.Driver
import com.das.taxi.common.networking.http.interfaces.HTTPRequest
import com.squareup.moshi.JsonClass

class Login(fireBaseToken: String): HTTPRequest() {
    override val path: String = "driver/login"
    init {
        this.params = mapOf("token" to fireBaseToken)
        Log.d("error_in_driver",fireBaseToken)
    }
}

@JsonClass(generateAdapter = true)
data class LoginResult(
    val token: String,
    val user: Driver
)