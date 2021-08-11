package com.brtrip.path.controller.response

import com.brtrip.place.Place

class PathResponse(
    val places: List<Place>,
    val likeCount: Long
)