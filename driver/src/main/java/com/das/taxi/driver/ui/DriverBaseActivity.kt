package com.das.taxi.driver.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.das.taxi.common.components.BaseActivity
import com.das.taxi.common.models.Request
import com.das.taxi.common.networking.socket.interfaces.SocketNetworkDispatcher
import com.das.taxi.common.utils.TravelRepository
import com.das.taxi.common.utils.TravelRepository.get
import com.das.taxi.common.utils.TravelRepository.set
import com.das.taxi.driver.activities.splash.SplashActivity

@SuppressLint("Registered")
open class DriverBaseActivity : BaseActivity() {
    protected var travel: Request?
        get() = get(this, TravelRepository.AppType.DRIVER)
        protected set(request) {
            set(this, TravelRepository.AppType.DRIVER, request!!)
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