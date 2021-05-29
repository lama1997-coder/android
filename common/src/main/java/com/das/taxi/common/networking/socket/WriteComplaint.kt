package com.das.taxi.common.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class WriteComplaint(requestId: Long, subject: String, content: String): SocketRequest() {
    init {
        this.params = arrayOf(requestId, subject, content)
    }
}