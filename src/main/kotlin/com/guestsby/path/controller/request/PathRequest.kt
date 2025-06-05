package com.guestsby.path.controller.request

import com.guestsby.place.PlaceRequest

class PathRequest(
    val id: Long?,
    val places: List<PlaceRequest>
)
