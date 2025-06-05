package com.guestsby.user.controller

import com.guestsby.auth.domain.UserPrincipal
import com.guestsby.common.response.ApiResponse
import com.guestsby.user.controller.request.UserUpdateRequest
import com.guestsby.user.controller.response.UserResponse
import com.guestsby.user.domain.LoginUser
import com.guestsby.user.domain.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun find(@LoginUser userPrincipal: UserPrincipal): ResponseEntity<ApiResponse> {
        val user = userService.find(userPrincipal.getId())
        return ResponseEntity
            .ok(ApiResponse(data = UserResponse.of(user)))
    }

    @PatchMapping("/me")
    fun update(
        @LoginUser userPrincipal: UserPrincipal,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<ApiResponse> {
        userService.update(userPrincipal.getId(), request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/me")
    fun delete(@LoginUser userPrincipal: UserPrincipal): ResponseEntity<ApiResponse> {
        userService.delete(userPrincipal.getId())
        return ResponseEntity.noContent().build()
    }
}
