package com.das.taxi.common.networking.socket

import com.das.taxi.common.networking.socket.interfaces.SocketRequest

class HideHistoryItem(requestId: Long) : SocketRequest() {
    init {
        this.params = arrayOf(requestId)
    }
}