package com.das.taxi.rider.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class UpdateProfileImage(data: ByteArray): SocketRequest() {
    init {
        this.params = arrayOf(data)
    }
}