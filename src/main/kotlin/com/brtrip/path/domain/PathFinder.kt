package com.brtrip.path.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.place.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathFinder(
    private val pathRepository: PathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
) {
    fun findByPlace(place: Place): List<Path> {
        val pathPlaces = pathPlaceRepository.findByPlaceAndDeleted(place, false)
        return pathPlaces.map { findById(it.path.id!!) }.distinct()
    }

    fun findById(id: Long): Path {
        return pathRepository.findByIdAndDeleted(id, false) ?: throw NotFoundException("Path가 존재하지 않습니다.")
    }
}
