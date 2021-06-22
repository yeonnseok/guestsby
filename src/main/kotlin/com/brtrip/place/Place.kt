package com.brtrip.place

import com.brtrip.common.BaseEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "lat", nullable = false)
    var lat: BigDecimal,

    @Column(name = "lng", nullable = false)
    var lng: BigDecimal,

    @Column(name = "title", nullable = false)
    var name: String
): BaseEntity()
