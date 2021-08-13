package com.brtrip.path

import com.brtrip.path.domain.PathPlace
import javax.persistence.*

@Entity
class Path(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "like_count")
    var likeCount: Long = 0
) {
    @OneToMany(mappedBy = "path")
    var pathPlaces: List<PathPlace>? = mutableListOf()
}
