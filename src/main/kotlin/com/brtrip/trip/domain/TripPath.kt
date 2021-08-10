package com.brtrip.link

import com.brtrip.common.BaseEntity
import com.brtrip.path.Path
import com.brtrip.trip.domain.Trip
import javax.persistence.*

@Entity
class TripPath(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var trip: Trip,

    @ManyToOne(fetch = FetchType.LAZY)
    var path: Path
): BaseEntity()