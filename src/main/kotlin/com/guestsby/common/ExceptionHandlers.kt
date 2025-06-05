package com.guestsby.common

import com.guestsby.common.exceptions.BadRequestException
import com.guestsby.common.exceptions.NotFoundException
import com.guestsby.common.response.ApiResponse
import com.guestsby.common.response.ErrorResponse
import com.guestsby.common.response.ResultType
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlers {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BadRequestException::class)
    fun badRequestErrorHandler(e: BadRequestException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.BAD_REQUEST.value(),
            data = ErrorResponse(e.message ?: "잘못된 요청입니다")
        )
        return ResponseEntity.badRequest()
            .header("Content-type", "application/json;charset=UTF-8")
            .body(error)
    }

    @ExceptionHandler(RuntimeException::class)
    fun internalServerErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = ErrorResponse(e.message ?: "서버에 문제가 발생했습니다")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .header("Content-type", "application/json;charset=UTF-8")
            .body(error)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.NOT_FOUND.value(),
            data = ErrorResponse(e.message ?: "요청하신 장소를 찾을 수 없습니다")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .header("Content-type", "application/json;charset=UTF-8")
            .body(error)
    }
}
