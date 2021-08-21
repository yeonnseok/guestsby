package com.brtrip.place

import com.brtrip.common.BaseEntity
import javax.persistence.*

@Entity
class PlaceCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    var place: Place
): BaseEntity()

