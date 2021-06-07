package com.brtrip.trip.controller

import com.brtrip.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class TripControllerTest : LoginUserControllerTest() {

    @Test
    fun `여행 일정 생성`() {
        // given
        val stop1 = mapOf(
                "lat" to "123",
                "lng" to "456",
                "name" to "central park",
                "stoppedAt" to "2021-06-03T00:00:00"
        )

        val stop2 = mapOf(
                "lat" to "789",
                "lng" to "101",
                "name" to "grand canyon",
                "stoppedAt" to "2021-06-04T00:00:00"
        )

        val body = mapOf(
                "title" to "first trip",
                "stops" to listOf(stop1, stop2),
                "memo" to "first trip",
                "startDate" to "2021-06-01",
                "endDate" to "2021-06-05"
        )

        // when
        val result = mockMvc.perform(
                post("/api/v1/trips")
                        .header("Authorization", "Bearer $token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
                .andExpect(status().isCreated)
                .andDo(
                        document(
                                "trip/create",
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰"),
                                        headerWithName("Content-Type").description("전송 타입")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("이메일"),
                                        fieldWithPath("stops[].lat").description("위도"),
                                        fieldWithPath("stops[].lng").description("경도"),
                                        fieldWithPath("stops[].name").description("장소 이름"),
                                        fieldWithPath("stops[].stoppedAt").description("방문 시각"),
                                        fieldWithPath("startDate").description("시작 일자"),
                                        fieldWithPath("endDate").description("종료 일자"),
                                        fieldWithPath("memo").description("메모")
                                )
                        )
                )
    }
}
