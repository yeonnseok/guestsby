package com.brtrip.trip.domain

import org.springframework.data.jpa.repository.JpaRepository

interface StopRepository : JpaRepository<Stop, Long>

