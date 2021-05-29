package com.das.taxi.rider.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class DeleteAddress(id: Int): SocketRequest() {
    init {
        this.params = arrayOf(id)
    }
}