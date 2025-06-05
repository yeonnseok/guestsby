package com.guestsby.common.exceptions

class NotFoundException(
        override val message: String?
) : RuntimeException()
