package com.brtrip.trip.domain

import com.brtrip.common.BaseEntity
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

    @Column(name = "memo")
    var memo: String? = null
) : BaseEntity() {
    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    var stops: MutableList<Stop> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }
}
