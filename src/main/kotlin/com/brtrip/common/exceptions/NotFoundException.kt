package com.brtrip.common.exceptions

class NotFoundException(
        override val message: String?
) : RuntimeException()
