package com.guestsby.user.controller.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserUpdateRequest(
    @NotBlank
    @Size(min = 2)
    val nickName: String
)
