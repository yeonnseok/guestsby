package com.guestsby.place

import com.guestsby.common.BaseEntity
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
    var content: String? = null,

    @Column(name = "rating")
    var rating: Int = 0,

    @Column(name = "address")
    var address: String? = null,

    @Column(name = "phone_number")
    var phoneNumber: String? = null
): BaseEntity() {
    @OneToMany(mappedBy = "place", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var placeCategories: MutableList<PlaceCategory> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }
}
