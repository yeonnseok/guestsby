package com.brtrip.place

import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal

interface PlaceRepository : JpaRepository<Place, Long> {
    fun findByLatAndLng(lat: BigDecimal, lng: BigDecimal): Place?
}
