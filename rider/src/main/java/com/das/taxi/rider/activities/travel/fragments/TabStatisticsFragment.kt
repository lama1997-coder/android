package com.das.taxi.rider.activities.travel.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.model.LatLng
import com.das.taxi.common.components.BaseFragment
import com.das.taxi.common.networking.socket.interfaces.SocketNetworkDispatcher
import com.das.taxi.common.utils.TravelRepository
import com.das.taxi.common.utils.TravelRepository.get
import com.das.taxi.rider.R
import com.das.taxi.rider.activities.travel.fragments.ReviewDialog.OnReviewFragmentInteractionListener
import com.das.taxi.rider.databinding.FragmentTravelStatsBinding
import java.text.NumberFormat
import java.util.*

class TabStatisticsFragment : BaseFragment() {
    private lateinit var binding: FragmentTravelStatsBinding
    private var listener: OnTravelInfoReceived? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val activity = activity ?: return
                activity.runOnUiThread {
                    val request = get(activity.applicationContext, TravelRepository.AppType.RIDER)
                    var timestamp: Long = 0
                    if (request.startTimestamp == null) {
                        if (request.etaPickup != null) {
                            timestamp = request.etaPickup!!
                        }
                    } else {
                        timestamp = request.startTimestamp!! + request.durationBest!! * 1000
                    }
                    val seconds = (timestamp - Date().time) / 1000
                    if (seconds <= 0) binding.etaText.setText(R.string.eta_soon) else binding.etaText.text = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60)
                }
            }
        }, 0, 1000)
        SocketNetworkDispatcher.instance.onTravelInfo = {
            if (listener != null) listener!!.onReceived(it)
            /*val request = get(context, TravelRepository.AppType.RIDER)
            val destination: LatLng = if (request.startTimestamp == null) request.getPickupPoint() else request.getDestinationPoint()
            val distance = distFrom(event.location!!, destination).toDouble()*/
            //if (binding.root.resources.getBoolean(R.bool.use_miles)) binding.distanceText.text = binding.root.context.getString(R.string.unit_distance_miles, distance / 1609.344f) else binding.distanceText.text = getString(R.string.unit_distance, distance / 1000f)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_travel_stats, container, false)
        val request = get(context, TravelRepository.AppType.RIDER)
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(request.currency)
        binding.costText.text = format.format(request.costBest)
        return binding.root
    }
    fun onUpdatePrice(price: Double) {
        val request = get(context, TravelRepository.AppType.RIDER)
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(request.currency)
        binding.costText.text = format.format(price)
    }

    interface OnTravelInfoReceived {
        fun onReceived(driverLocation: LatLng?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnReviewFragmentInteractionListener) {
            context as OnTravelInfoReceived
        } else {
            throw RuntimeException("$context must implement onEditAddressInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        fun newInstance(): TabStatisticsFragment {
            return TabStatisticsFragment()
        }
    }
}