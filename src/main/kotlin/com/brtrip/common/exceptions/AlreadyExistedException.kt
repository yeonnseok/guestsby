package com.brtrip.common.exceptions

class AlreadyExistedException(
    override val message: String?
): RuntimeException()