package com.brtrip.like.domain

import com.brtrip.trip.domain.Trip
import com.brtrip.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun findByUserAndTrip(user: User, trip: Trip): Like?
}
