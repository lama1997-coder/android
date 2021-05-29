package com.das.taxi.common.networking.http.interfaces

import com.das.taxi.common.networking.socket.interfaces.RemoteResponse

interface HTTPNetworkDispatcherBase {
    fun dispatch(path: String, params: Map<String, Any>?, completionHandler: (RemoteResponse<Any, HTTPStatusCode>) -> Unit)
}