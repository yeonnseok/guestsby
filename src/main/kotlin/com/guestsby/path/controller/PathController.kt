package com.guestsby.path.controller

import com.guestsby.common.response.ApiResponse
import com.guestsby.path.controller.request.PathRequest
import com.guestsby.path.domain.PathService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/paths")
class PathController(
    private val pathService: PathService
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: PathRequest
    ): ResponseEntity<Void> {
        val pathId = pathService.create(request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/paths/$pathId")
            .buildAndExpand(pathId).toUri()

        return ResponseEntity.created(location).build()
    }
}
