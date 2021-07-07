package com.brtrip.place

import com.brtrip.common.exceptions.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PlaceFinder(
    private val placeRepository: PlaceRepository
) {
    fun findByPosition(lat: String, lng: String): Place {
        return placeRepository.findByLatAndLng(lat, lng)
            ?: throw NotFoundException("장소를 찾을 수 없습니다.")
    }

}