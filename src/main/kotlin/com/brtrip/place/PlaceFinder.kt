package com.brtrip.place

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.Path
import com.brtrip.path.domain.PathPlaceRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PlaceFinder(
    private val placeRepository: PlaceRepository,
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findByPosition(lat: String, lng: String): Place {
        return placeRepository.findByLatAndLng(lat, lng)
            ?: throw NotFoundException("장소를 찾을 수 없습니다.")
    }

    fun findByPath(path: Path): List<Place> {
        val pathPlaces = pathPlaceRepository.findByPath(path)
        return pathPlaces.map {
            it.place
        }
    }
}