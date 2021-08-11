package com.brtrip.place

data class PlaceRequest(
    var lat: String,
    var lng: String,
    var name: String,
    var content: String? = null
)