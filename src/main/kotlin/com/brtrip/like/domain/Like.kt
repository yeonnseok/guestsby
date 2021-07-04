package com.brtrip.like.domain

import com.brtrip.common.BaseEntity
import com.brtrip.trip.domain.Trip
import com.brtrip.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "likes")
class Like(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    var trip: Trip,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
): BaseEntity()
