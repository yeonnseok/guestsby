package com.brtrip.path.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PathPlaceRepository : JpaRepository<PathPlace, Long> {
    fun findByPathId(pathId: Long): List<PathPlace>
    fun findByPlaceId(placeId: Long): List<PathPlace>
}