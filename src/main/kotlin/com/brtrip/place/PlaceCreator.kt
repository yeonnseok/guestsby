package com.brtrip.place

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
            request.keywords!!.map {
                PlaceCategory(null, Category(null, it!!), place, false)
            }?.toMutableList().also { place.placeCategories = it!! }

            return placeRepository.save(place)
        }
        return placeExist
    }
}
