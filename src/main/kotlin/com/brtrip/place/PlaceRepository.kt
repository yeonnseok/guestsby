package com.brtrip.place

import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository : JpaRepository<Place, Long> {
    fun findByLatAndLngAndName(lat: Long, lng: Long, name: String): Place?
}
