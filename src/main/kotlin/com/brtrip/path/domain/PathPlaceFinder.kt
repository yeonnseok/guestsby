package com.brtrip.path.domain

import com.brtrip.path.Path
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathPlaceFinder(
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findBy(path: Path): List<PathPlace> {
        return pathPlaceRepository.findByPath(path)
    }
}