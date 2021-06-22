package com.brtrip.place

import com.brtrip.common.BaseEntity
import javax.persistence.*

@Entity
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "lat", nullable = false)
    var lat: Long,

    @Column(name = "lng", nullable = false)
    var lng: Long,

    @Column(name = "title", nullable = false)
    var name: String
): BaseEntity()
