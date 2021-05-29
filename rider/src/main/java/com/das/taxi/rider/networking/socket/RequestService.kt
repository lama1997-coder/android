package com.das.taxi.rider.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest
import com.das.taxi.common.utils.Adapters
import com.das.taxi.rider.models.RequestDTO
import org.json.JSONObject

class RequestService(requestDto: RequestDTO): SocketRequest() {
    init {
        val dto = JSONObject(Adapters.moshi.adapter<RequestDTO>(RequestDTO::class.java).toJson(requestDto))
        this.params = arrayOf(dto)
    }
}


