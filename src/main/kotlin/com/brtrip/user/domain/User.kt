package com.brtrip.user.domain

import com.brtrip.common.BaseEntity
import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nick_name", nullable = true)
    var nickName: String? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: RoleType,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider: AuthProvider
) : BaseEntity()
