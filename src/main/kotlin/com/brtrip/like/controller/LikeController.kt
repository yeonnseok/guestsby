package com.brtrip.like.controller

import com.brtrip.like.domain.LikeService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/likes")
class LikeController(
    private val likeService: LikeService
) {
}