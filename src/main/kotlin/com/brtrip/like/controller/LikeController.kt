package com.brtrip.like.controller

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.like.controller.request.LikeRequest
import com.brtrip.like.domain.LikeService
import com.brtrip.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/likes")
class LikeController(
    private val likeService: LikeService
) {
    @PostMapping
    fun create(
        @LoginUser userPrincipal: UserPrincipal,
        @RequestBody request: LikeRequest
    ): ResponseEntity<Void> {
        val like = likeService.like(userPrincipal.getId(), request)
        return ResponseEntity
            .created(URI("/api/v1/likes/${like.id!!}"))
            .build()
    }

    @DeleteMapping
    fun delete(
        @LoginUser userPrincipal: UserPrincipal,
        @RequestBody request: LikeRequest
    ): ResponseEntity<Void> {
        likeService.delete(userPrincipal.getId(), request)
        return ResponseEntity.noContent().build()
    }
}