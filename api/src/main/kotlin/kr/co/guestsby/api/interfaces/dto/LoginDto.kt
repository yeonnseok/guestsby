package kr.co.guestsby.api.interfaces.dto

object LoginDto {

    data class Request(
        val email: String,
        val password: String
    )

    data class Response(
        val token: String
    )
}
