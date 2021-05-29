package com.das.taxi.rider.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class ApplyCoupon(code: String) : SocketRequest() {
    init {
        this.params = arrayOf(code)
    }
}