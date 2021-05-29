package com.das.taxi.rider.ui

import android.content.Intent
import android.os.Bundle
import com.das.taxi.common.components.BaseActivity
import com.das.taxi.common.models.Request
import com.das.taxi.common.networking.socket.interfaces.SocketNetworkDispatcher
import com.das.taxi.common.utils.TravelRepository
import com.das.taxi.common.utils.TravelRepository.get
import com.das.taxi.common.utils.TravelRepository.set
import com.das.taxi.rider.activities.splash.SplashActivity

abstract class RiderBaseActivity : BaseActivity() {
    var travel: Request?
        get() = get(this, TravelRepository.AppType.RIDER)
        protected set(request) {
            set(this, TravelRepository.AppType.RIDER, request!!)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(SocketNetworkDispatcher.instance.socket == null) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }
    }
}