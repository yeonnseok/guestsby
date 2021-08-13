package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
import com.brtrip.path.domain.PathPlace
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Trip(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var userId: Long,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDate,

    @Column(name = "memo")
    var memo: String? = null
) : BaseEntity() {
    @OneToMany(mappedBy = "trip", targetEntity = TripPath::class)
    var tripPaths: List<TripPath>? = mutableListOf()
}
