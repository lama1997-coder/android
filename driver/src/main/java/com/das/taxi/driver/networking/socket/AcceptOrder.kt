package com.das.taxi.driver.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class AcceptOrder(requestId: Long) : SocketRequest() {
    init {
        this.params = arrayOf(requestId)
    }
}