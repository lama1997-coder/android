package com.das.taxi.driver.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class UpdateStatus(turnOnline: Boolean): SocketRequest() {
    init {
        this.params = arrayOf(turnOnline)
    }
}