package com.guestsby.path.domain

import com.guestsby.place.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PathPlaceRepository : JpaRepository<PathPlace, Long> {
    fun findByPathAndDeleted(path: Path, delete: Boolean): List<PathPlace>

    fun findByPlaceAndDeleted(place: Place, delete: Boolean): List<PathPlace>
}
