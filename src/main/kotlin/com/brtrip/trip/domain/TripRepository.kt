package com.brtrip.trip.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TripRepository : JpaRepository<Trip, Long> {

    fun findByUserId(userId: Long): List<Trip>

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: Long): Trip?
}
