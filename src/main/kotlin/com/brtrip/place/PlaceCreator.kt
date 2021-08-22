package com.brtrip.place

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class PlaceCreator(
    private val placeRepository: PlaceRepository
) {
    fun create(request: PlaceRequest): Place {
        return placeRepository.findByLatAndLngAndDeleted(request.lat, request.lng, false)
            ?: placeRepository.save(request.toEntity())
    }
}
