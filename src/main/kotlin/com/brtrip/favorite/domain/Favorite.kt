package com.brtrip.favorite.domain

import com.brtrip.common.BaseEntity
import com.brtrip.path.Path
import com.brtrip.user.domain.User
import javax.persistence.*

@Entity
class Favorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id")
    var path: Path
): BaseEntity()