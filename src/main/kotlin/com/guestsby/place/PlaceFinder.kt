package com.guestsby.place

import com.guestsby.common.exceptions.NotFoundException
import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlaceRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PlaceFinder(
    private val placeRepository: PlaceRepository,
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findByPosition(lat: String, lng: String): Place {
        return placeRepository.findByLatAndLngAndDeleted(lat, lng, false)
            ?: throw NotFoundException("장소를 찾을 수 없습니다.")
    }

    fun findByPath(path: Path): List<Place> {
        return pathPlaceRepository.findByPathAndDeleted(path, false)
            .sortedBy { it.sequence }
            .map { it.place }
    }

    fun findById(id: Long): Place {
        return placeRepository.findByIdAndDeleted(id, false)
            ?: throw NotFoundException("장소를 찾을 수 없습니다.")
    }
}
