package com.guestsby.common.exceptions

import java.lang.RuntimeException

class AuthorizationException(
    override val message: String?
) : RuntimeException()
