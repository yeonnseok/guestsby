package com.brtrip.path

import javax.persistence.*

@Entity
class Path(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "like_count")
    var likeCount: Long = 0
)