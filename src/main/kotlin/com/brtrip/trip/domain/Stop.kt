package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
import com.brtrip.place.Place
import javax.persistence.*

@Entity
data class Stop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    var trip: Trip,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "place_id")
    var place: Place,

    @Column(name = "sequence", nullable = false)
    var sequence: Int
) : BaseEntity()
