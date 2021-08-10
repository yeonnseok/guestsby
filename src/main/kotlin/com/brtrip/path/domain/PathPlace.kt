package com.brtrip.path.domain

import com.brtrip.common.BaseEntity
import com.brtrip.path.Path
import com.brtrip.place.Place
import javax.persistence.*

@Entity
class PathPlace(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "path_id")
    var path: Path,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "place_id")
    var place: Place,

    @Column(name = "sequence", nullable = false)
    var sequence: Long
): BaseEntity()