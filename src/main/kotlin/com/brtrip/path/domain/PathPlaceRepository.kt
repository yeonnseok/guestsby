package com.brtrip.path.domain

import com.brtrip.path.Path
import com.brtrip.place.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PathPlaceRepository : JpaRepository<PathPlace, Long> {
    fun findByPath(path: Path): List<PathPlace>
    fun findByPlace(place: Place): List<PathPlace>
}