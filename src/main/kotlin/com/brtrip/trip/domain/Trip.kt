package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
import com.brtrip.user.domain.User
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

    @Column(name = "startDate", nullable = false)
    var startDate: LocalDate,

    @Column(name = "endDate", nullable = false)
    var endDate: LocalDate,

    @Column(name = "memo")
    var memo: String? = null,

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    var stops: List<Stop>? = null
) : BaseEntity()
