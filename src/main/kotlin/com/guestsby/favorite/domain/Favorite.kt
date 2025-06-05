package com.guestsby.favorite.domain

import com.guestsby.common.BaseEntity
import com.guestsby.path.domain.Path
import com.guestsby.user.domain.User
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
