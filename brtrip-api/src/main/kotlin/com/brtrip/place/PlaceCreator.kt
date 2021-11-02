package com.brtrip.place

import com.brtrip.place.controller.request.PlaceRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class PlaceCreator(
    private val placeRepository: PlaceRepository
) {
    fun create(request: PlaceRequest): Place {
        val placeExist = placeRepository.findByLatAndLngAndDeleted(request.lat, request.lng, false)
        if (placeExist == null) {
            val place = request.toEntity()
            var isRepresentative = true
            request.keywords.mapIndexed { index, data ->
                if (index != 0) isRepresentative = false
                PlaceCategory(null, Category(null, data!!), place, isRepresentative)
            }.toMutableList().also { place.placeCategories = it }

            return placeRepository.save(place)
        }
        return placeExist
    }
}
