package com.das.taxi.rider.activities.promotions

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.das.taxi.common.components.BaseActivity
import com.das.taxi.common.models.Promotion
import com.das.taxi.common.networking.socket.interfaces.RemoteResponse
import com.das.taxi.common.utils.AlerterHelper
import com.das.taxi.rider.R
import com.das.taxi.rider.activities.promotions.adapters.PromotionsRecyclerViewAdapter
import com.das.taxi.rider.databinding.ActivityPromotionsBinding
import com.das.taxi.rider.networking.socket.GetPromotions
import com.tylersuehr.esr.ContentItemLoadingStateFactory
import com.tylersuehr.esr.EmptyStateRecyclerView
import com.tylersuehr.esr.ImageTextStateDisplay

class PromotionsActivity : BaseActivity() {
    lateinit var binding: ActivityPromotionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if(preferences.language.equals("ar")){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){

                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;}
        }
        else
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PromotionsActivity, R.layout.activity_promotions)
        initializeToolbar(getString(R.string.drawer_promotions))
        binding.recyclerView.setStateDisplay(EmptyStateRecyclerView.STATE_LOADING, ContentItemLoadingStateFactory.newListLoadingState(this))
        binding.recyclerView.setStateDisplay(EmptyStateRecyclerView.STATE_EMPTY, ImageTextStateDisplay(this, com.das.taxi.common.R.drawable.empty_state, getString(R.string.empty_state_title), getString(R.string.empty_state_message)))
        binding.recyclerView.setStateDisplay(EmptyStateRecyclerView.STATE_ERROR, ImageTextStateDisplay(this, com.das.taxi.common.R.drawable.empty_state, getString(R.string.empty_state_error_title), getString(R.string.empty_state_error_message)))
        binding.recyclerView.invokeState(EmptyStateRecyclerView.STATE_LOADING)
        refreshPromotions()
    }

    private fun refreshPromotions() {
        GetPromotions().executeArray<Promotion> {
            when(it) {
                is RemoteResponse.Success -> {
                    if (it.body.isEmpty()) {
                        binding.recyclerView.invokeState(EmptyStateRecyclerView.STATE_EMPTY)
                        return@executeArray
                    }
                    binding.recyclerView.invokeState(EmptyStateRecyclerView.STATE_OK)
                    val promotionsRecyclerViewAdapter = PromotionsRecyclerViewAdapter(it.body)
                    val llm = LinearLayoutManager(this@PromotionsActivity)
                    llm.orientation = LinearLayoutManager.VERTICAL
                    binding.recyclerView.setHasFixedSize(true)
                    binding.recyclerView.layoutManager = llm
                    binding.recyclerView.adapter = promotionsRecyclerViewAdapter
                }

                is RemoteResponse.Error -> {
                    AlerterHelper.showError(this, it.error.message)
                }
            }

        }

    }
}