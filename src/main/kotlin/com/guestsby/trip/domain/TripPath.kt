package com.guestsby.trip.domain

import com.guestsby.common.BaseEntity
import com.guestsby.path.domain.Path
import javax.persistence.*

@Entity
@Table(name = "trip_path")
class TripPath(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    var trip: Trip,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id")
    var path: Path,

    var sequence: Int
): BaseEntity()
