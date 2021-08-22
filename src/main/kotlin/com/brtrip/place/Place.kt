package com.brtrip.place

import com.brtrip.common.BaseEntity
import com.brtrip.path.domain.PathPlace
import javax.persistence.*

@Entity
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "lat", nullable = false)
    var lat: String,

    @Column(name = "lng", nullable = false)
    var lng: String,

    @Column(name = "title")
    var name: String,

    @Column(name = "content")
    var content: String? = null
): BaseEntity() {
    @OneToMany(mappedBy = "place", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var placeCategories: MutableList<PlaceCategory> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }
}
