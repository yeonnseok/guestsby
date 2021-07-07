package com.brtrip.like.controller

import com.brtrip.TestDataLoader
import com.brtrip.like.domain.Like
import com.brtrip.like.domain.LikeRepository
import com.brtrip.place.Place
import com.brtrip.restdocs.LoginUserControllerTest
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.StopRepository
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripRepository
import com.brtrip.user.domain.UserFinder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

internal class LikeControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Autowired
    private lateinit var likeRepository: LikeRepository

    @Autowired
    private lateinit var userFinder: UserFinder

    @Test
    fun `좋아요 하기`() {
        // given
        val trip = testDataLoader.sample_trip_first(userId!!)
        testDataLoader.sample_stops_first(trip)

        val body = mapOf(
            "tripId" to trip.id!!
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/likes")
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
                    "like/create",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("tripId").description("찜할 여행 일정 ID")
                    )
                )
            )
    }

    @Test
    fun `좋아요 취소하기`() {
        // given
        val trip = testDataLoader.sample_trip_first(userId!!)
        testDataLoader.sample_stops_first(trip)

        val user = userFinder.findById(userId!!)
        likeRepository.save(Like(trip = trip, user = user))

        val body = mapOf(
            "tripId" to trip.id!!
        )

        // when
        val result = mockMvc.perform(
            delete("/api/v1/likes")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "like/delete",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("tripId").description("찜할 여행 일정 ID")
                    )
                )
            )
    }
}