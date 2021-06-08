package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Stop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    var trip: Trip,

    @Column(name = "lat", nullable = false)
    var lat: Long,

    @Column(name = "lng", nullable = false)
    var lng: Long,

    @Column(name = "title", nullable = false)
    var name: String,

    @Column(name = "stoppedAt", nullable = false)
    var stoppedAt: LocalDateTime,

    @Column(name = "sequence", nullable = false)
    var sequence: Int
) : BaseEntity()
