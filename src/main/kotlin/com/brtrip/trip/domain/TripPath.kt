package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
import com.brtrip.path.Path
import javax.persistence.*

@Entity
class TripPath(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "trip_id")
    var trip: Trip,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "path_id")
    var path: Path
): BaseEntity()