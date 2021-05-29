package com.das.taxi.rider.networking.socket

import com.das.taxi.common.models.Address
import com.das.taxi.common.networking.socket.interfaces.SocketRequest
import com.das.taxi.common.utils.Adapters
import org.json.JSONObject

class UpsertAddress(address: Address): SocketRequest() {
    init {
        val add = JSONObject(Adapters.moshi.adapter<Address>(Address::class.java).toJson(address))
        this.params = arrayOf(add)
    }
}