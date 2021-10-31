package com.brtrip.common

import com.brtrip.common.exceptions.BadRequestException
import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.common.response.ApiResponse
import com.brtrip.common.response.ErrorResponse
import com.brtrip.common.response.ResultType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(RuntimeException::class)
    fun internalServerErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = ErrorResponse(e.message ?: "서버에 문제가 발생했습니다")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.NOT_FOUND.value(),
            data = ErrorResponse(e.message ?: "요청하신 장소를 찾을 수 없습니다")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }
}
