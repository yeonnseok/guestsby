package com.guestsby.place

import com.guestsby.common.BaseEntity
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class PlaceCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "category_id")
    var category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    var place: Place,

    @Type(type="yes_no")
    @Column(name = "is_representative", nullable = false)
    var isRepresentative: Boolean = false
): BaseEntity()

