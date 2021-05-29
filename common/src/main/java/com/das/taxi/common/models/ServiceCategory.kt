package com.das.taxi.common.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ServiceCategory {
    var title: String? = null
    var id = 0
    var services: List<Service>? = null

}