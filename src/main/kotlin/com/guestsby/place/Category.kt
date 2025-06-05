package com.guestsby.place

import com.guestsby.common.BaseEntity
import javax.persistence.*

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String
): BaseEntity()
