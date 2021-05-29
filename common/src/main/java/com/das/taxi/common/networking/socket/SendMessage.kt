package com.das.taxi.common.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class SendMessage(content: String) : SocketRequest() {
    init {
        this.params = arrayOf(content)
    }
}