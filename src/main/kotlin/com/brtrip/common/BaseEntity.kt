package com.brtrip.common

import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class BaseEntity{

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(columnDefinition = "timestamp")
    var updatedAt  : LocalDateTime = LocalDateTime.now()

    @Type(type="yes_no")
    @Column(name = "delete_flag", nullable = false)
    var deleted: Boolean = false

    fun delete() {
        deleted = true
    }
}
