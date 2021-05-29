package com.das.taxi.rider.activities.travel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.databinding.DataBindingUtil
import com.das.taxi.common.components.BaseFragment
import com.das.taxi.common.models.Review
import com.das.taxi.common.networking.socket.interfaces.EmptyClass
import com.das.taxi.common.networking.socket.interfaces.RemoteResponse
import com.das.taxi.rider.R
import com.das.taxi.rider.databinding.FragmentTravelReviewBinding
import com.das.taxi.rider.networking.socket.ReviewDriver

class TabReviewFragment : BaseFragment() {
    lateinit var binding: FragmentTravelReviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_travel_review, container, false)
        binding.ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar: RatingBar?, v: Float, b: Boolean -> binding.buttonSaveReview.isEnabled = true }
        binding.buttonSaveReview.setOnClickListener { view: View? ->
            val review = Review(binding.ratingBar.rating.toInt() * 20, binding.reviewText.text.toString(), 0)
            ReviewDriver(review).execute<EmptyClass> {
                when(it) {
                    is RemoteResponse.Success -> {

                    }

                    is RemoteResponse.Error -> {

                    }
                }

            }
        }
        return binding.root
    }
}