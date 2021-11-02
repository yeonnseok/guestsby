package com.brtrip.auth.domain.dto

import javax.validation.constraints.NotBlank

data class EmailCheckRequest(
    @NotBlank
    val email: String
)
