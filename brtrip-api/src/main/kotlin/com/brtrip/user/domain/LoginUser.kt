package com.brtrip.user.domain

import org.springframework.security.core.annotation.AuthenticationPrincipal


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AuthenticationPrincipal
annotation class LoginUser
