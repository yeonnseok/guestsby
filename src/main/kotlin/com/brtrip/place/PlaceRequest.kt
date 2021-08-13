package com.brtrip.place

data class PlaceRequest(
    var lat: String,
    var lng: String,
    var name: String? = null,
    var content: String? = null
)
