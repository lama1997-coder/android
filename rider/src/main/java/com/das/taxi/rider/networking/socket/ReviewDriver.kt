package com.das.taxi.rider.networking.socket

import com.das.taxi.common.models.Review
import com.das.taxi.common.networking.socket.interfaces.SocketRequest
import com.das.taxi.common.utils.Adapters
import org.json.JSONObject

class ReviewDriver(review: Review): SocketRequest() {
    init {
        val obj = JSONObject(Adapters.moshi.adapter<Review>(Review::class.java).toJson(review))
        this.params = arrayOf(obj)
    }
}