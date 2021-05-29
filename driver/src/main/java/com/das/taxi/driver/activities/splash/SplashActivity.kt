package com.das.taxi.driver.activities.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.das.taxi.common.components.BaseActivity
import com.das.taxi.common.interfaces.AlertDialogEvent
import com.das.taxi.common.networking.socket.interfaces.ConnectionError
import com.das.taxi.common.networking.socket.interfaces.Namespace
import com.das.taxi.common.networking.socket.interfaces.RemoteResponse
import com.das.taxi.common.networking.socket.interfaces.SocketNetworkDispatcher
import com.das.taxi.common.utils.AlertDialogBuilder
import com.das.taxi.common.utils.AlertDialogBuilder.DialogResult
import com.das.taxi.common.utils.AlertDialogBuilder.show
import com.das.taxi.common.utils.AlerterHelper.showError
import com.das.taxi.common.utils.CommonUtils.isInternetDisabled
import com.das.taxi.common.utils.MyPreferenceManager.Companion.getInstance
import com.das.taxi.driver.R
import com.das.taxi.driver.activities.main.MainActivity
import com.das.taxi.driver.activities.profile.ProfileActivity
import com.das.taxi.driver.databinding.ActivitySplashBinding
import com.das.taxi.driver.networking.http.GetRegisterInfo
import com.das.taxi.driver.networking.http.Login
import com.das.taxi.driver.networking.http.LoginResult
import com.das.taxi.driver.networking.http.RegistrationInfo
import java.util.*
import kotlin.collections.ArrayList

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var SIGN_IN_ACTIVITY = 123
    private var startRequested = false
    lateinit var locale: Locale
//    var currentLang = preferences.language

    private val onLoginClicked = View.OnClickListener {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(listOf(PhoneBuilder().build()))
                        .setLogo(R.drawable.logo)
                        .setTheme(currentTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                SIGN_IN_ACTIVITY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shouldReconnect = false
        immersiveScreen = true
        super.onCreate(savedInstanceState)



        if(preferences.token==null){
        preferences.putBoolean("first_time",true)}

        Log.d("language_first_time",preferences.language + preferences.getBoolean("first_time",false))
        if(preferences.getBoolean("first_time",false)){
            preferences.putBoolean("first_time",false)

            preferences.language=Locale.getDefault().language

            if( preferences.language.equals("en"))
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
            else
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;


            setLocale(Locale.getDefault().language)


        }
        else{
            setLocale(preferences.language!!)
            if( preferences.language.equals("en"))
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
            else
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;



        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)




        binding.loginButton.setOnClickListener(onLoginClicked)


    }



    private fun setLocale(localeName: String) {



            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                    this,
                    MainActivity::class.java
            )

    }

    private fun checkPermissions() {
        if (isInternetDisabled(this)) {
            show(this, getString(R.string.message_internet_connection), AlertDialogBuilder.DialogButton.CANCEL_RETRY, AlertDialogEvent { result: DialogResult ->
                if (result === DialogResult.RETRY) {
                    checkPermissions()
                } else {
                    finishAffinity()
                }
            })
            return
        }

        if(getInstance(applicationContext).token != null) {
            Log.d("tokenApiDriver",getInstance(applicationContext).token)
            tryConnect(getInstance(applicationContext).token!!)
        } else {
            goToLoginMode()
        }
    }

    private fun tryConnect(jwtToken: String) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {fb ->
            SocketNetworkDispatcher.instance.connect(Namespace.Driver, jwtToken, fb.result?.token ?: "") {

                when (it) {
                    is RemoteResponse.Success -> {


                        Log.d("error_response_success","1")
                        startMainActivity()
                    }

                    is RemoteResponse.Error -> {
                        Log.d("error_response_error","1")

                        when (it.error) {
                            ConnectionError.RegistrationIncomplete -> {
                                runOnUiThread {
                                    showRegisterForm(jwtToken)
                                }
                            }

                            else -> {
                                runOnUiThread {
                                    goToLoginMode()
                                    try {
                                        it.error.showAlert(this)
                                    } catch (exception: Exception) {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showRegisterForm(jwtToken: String) {
        GetRegisterInfo(jwtToken).execute<RegistrationInfo> {
            when(it) {

                is RemoteResponse.Success -> {
                    runOnUiThread {
                        preferences.driver = it.body.driver
                        preferences.services = ArrayList(it.body.services)
                        val intent = Intent(this@SplashActivity, ProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }

                is RemoteResponse.Error -> {
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun startMainActivity() {
        if (startRequested) return
        startRequested = true
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun tryLogin(firebaseToken: String) {
        goToLoadingMode()
        Login(firebaseToken).execute<LoginResult> {
            when(it) {
                is RemoteResponse.Success -> {


                    Log.e("error_in_driver",
                            it.toString())
                    getInstance(applicationContext).driver = it.body.user
                    getInstance(applicationContext).token = it.body.token
                    tryConnect(it.body.token)
                }
                is RemoteResponse.Error -> {
                    showError(this, it.error.localizedDescription)
                    Log.e("error_in_driver",
                           it.toString())
                }
            }
        }
    }

    private fun goToLoadingMode() {
        binding.loginButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun goToLoginMode() {
        binding.loginButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                FirebaseAuth.getInstance().currentUser!!.getIdToken(false).addOnCompleteListener {
                    tryLogin(it.result!!.token!!)
                }
                return
            }
            goToLoginMode()
        }
    }
}