package com.guestsby.path.domain

import com.guestsby.place.Place
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathPlaceFinder(
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findByPath(path: Path): List<PathPlace> {
        return pathPlaceRepository.findByPathAndDeleted(path, false)
    }

    fun findByPlace(place: Place): List<PathPlace> {
        return pathPlaceRepository.findByPlaceAndDeleted(place, false)
    }
}
