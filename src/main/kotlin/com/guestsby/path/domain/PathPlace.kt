package com.guestsby.path.domain

import com.guestsby.common.BaseEntity
import com.guestsby.place.Place
import javax.persistence.*

@Entity
@Table(name = "path_place")
class PathPlace(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id")
    var path: Path,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    var place: Place,

    @Column(name = "sequence", nullable = false)
    var sequence: Int
): BaseEntity()
