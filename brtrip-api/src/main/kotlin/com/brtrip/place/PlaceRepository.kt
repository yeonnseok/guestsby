package com.brtrip.place

import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository : JpaRepository<Place, Long> {
    fun findByLatAndLngAndDeleted(lat: String, lng: String, deleted: Boolean): Place?

    fun findByIdAndDeleted(id: Long, delete: Boolean): Place?
}
