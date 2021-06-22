package com.brtrip.trip.controller

import com.brtrip.common.response.ResultType
import com.brtrip.place.Place
import com.brtrip.restdocs.LoginUserControllerTest
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.StopRepository
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

class TripControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository


    @Test
    fun `여행 일정 생성`() {
        // given
        val stop1 = mapOf(
            "lat" to "123",
            "lng" to "456",
            "name" to "central park"
        )

        val stop2 = mapOf(
            "lat" to "789",
            "lng" to "101",
            "name" to "grand canyon"
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
                        fieldWithPath("title").description("여행 일정 제목"),
                        fieldWithPath("stops[].lat").description("위도"),
                        fieldWithPath("stops[].lng").description("경도"),
                        fieldWithPath("stops[].name").description("장소 이름"),
                        fieldWithPath("startDate").description("시작 일자"),
                        fieldWithPath("endDate").description("종료 일자"),
                        fieldWithPath("memo").description("메모")
                    )
                )
            )
    }

    @Test
    fun `내 모든 여행 일정 조회`() {
        // given
        val trips = tripRepository.saveAll(
            listOf(
                Trip(
                    userId = userId!!,
                    title = "first trip",
                    startDate = LocalDate.of(2021, 6, 1),
                    endDate = LocalDate.of(2021, 6, 5)
                ),
                Trip(
                    userId = userId!!,
                    title = "second trip",
                    startDate = LocalDate.of(2021, 8, 1),
                    endDate = LocalDate.of(2021, 8, 1)
                )
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trips[0],
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trips[0],
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                ),
                Stop(
                    trip = trips[1],
                    place = Place(
                        name = "rainbow cafe",
                        lat = BigDecimal(987),
                        lng = BigDecimal(654),
                    ),
                    sequence = 1
                )
            )
        )

        // when
        val result = mockMvc.perform(
            get("/api/v1/trips/my")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andDo(
                document(
                    "trip/find-all-my",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data[].title").description("여행 일정 제목"),
                        fieldWithPath("data[].stops[].lat").description("위도"),
                        fieldWithPath("data[].stops[].lng").description("경도"),
                        fieldWithPath("data[].stops[].name").description("장소 이름"),
                        fieldWithPath("data[].stops[].sequence").description("일정 순서"),
                        fieldWithPath("data[].startDate").description("시작 일자"),
                        fieldWithPath("data[].endDate").description("종료 일자"),
                        fieldWithPath("data[].memo").description("메모")
                    )
                )
            )
    }

    @Test
    fun `내 최근 여행 일정 조회`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = userId!!,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        // when
        val result = mockMvc.perform(
            get("/api/v1/trips/recent")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.title").value("first trip"))
            .andExpect(jsonPath("data.stops[0].lat").value(123))
            .andExpect(jsonPath("data.stops[0].lng").value(456))
            .andExpect(jsonPath("data.stops[0].name").value("central park"))
            .andExpect(jsonPath("data.startDate").value("2021-06-01"))
            .andExpect(jsonPath("data.endDate").value("2021-06-05"))
            .andExpect(jsonPath("data.memo").isEmpty)
            .andDo(
                document(
                    "trip/find-recent",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.title").description("여행 일정 제목"),
                        fieldWithPath("data.stops[].lat").description("위도"),
                        fieldWithPath("data.stops[].lng").description("경도"),
                        fieldWithPath("data.stops[].name").description("장소 이름"),
                        fieldWithPath("data.stops[].sequence").description("일정 순서"),
                        fieldWithPath("data.startDate").description("시작 일자"),
                        fieldWithPath("data.endDate").description("종료 일자"),
                        fieldWithPath("data.memo").description("메모")
                    )
                )
            )
    }

    @Test
    fun `여행 일정 수정`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = userId!!,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        val stop2 = mapOf(
            "lat" to "789",
            "lng" to "101",
            "name" to "grand canyon"
        )

        val stop3 = mapOf(
            "lat" to "112",
            "lng" to "131",
            "name" to "rainbow cafe"
        )

        val body = mapOf(
            "title" to "new trip",
            "stops" to listOf(stop2, stop3),
            "memo" to null,
            "startDate" to "2021-06-02",
            "endDate" to "2021-06-06"
        )

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/v1/trips/{id}", trip.id)
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
                    "trip/update",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    pathParameters(
                        parameterWithName("id").description("여행 일정 ID")
                    ),
                    requestFields(
                        fieldWithPath("title").description("여행 일정 제목"),
                        fieldWithPath("stops[].lat").description("위도"),
                        fieldWithPath("stops[].lng").description("경도"),
                        fieldWithPath("stops[].name").description("장소 이름"),
                        fieldWithPath("startDate").description("시작 일자"),
                        fieldWithPath("endDate").description("종료 일자"),
                        fieldWithPath("memo").description("메모")
                    )
                )
            )
    }

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = userId!!,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/v1/trips/{id}", trip.id)
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // then
        result
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "trip/delete",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    pathParameters(
                        parameterWithName("id").description("여행 일정 ID")
                    )
                )
            )
    }

    @Test
    fun `특정 장소가 포함된 경로의 trip 불러오기`() {
        val trip = tripRepository.save(
            Trip(
                userId = userId!!,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        // when
        val result = mockMvc.perform(
            get("/api/v1/trips?lat=789&lng=101")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data[0].title").value("first trip"))
            .andExpect(jsonPath("data[0].stops[0].lat").value(123))
            .andExpect(jsonPath("data[0].stops[0].lng").value(456))
            .andExpect(jsonPath("data[0].stops[0].name").value("central park"))
            .andExpect(jsonPath("data[0].startDate").value("2021-06-01"))
            .andExpect(jsonPath("data[0].endDate").value("2021-06-05"))
            .andExpect(jsonPath("data[0].memo").isEmpty)
            .andDo(
                document(
                    "trip/find-recommendation",
                    requestParameters(
                        parameterWithName("lat").description("포함할 장소의 위도"),
                        parameterWithName("lng").description("포함할 장소의 경도")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data[].title").description("여행 일정 제목"),
                        fieldWithPath("data[].stops[].lat").description("위도"),
                        fieldWithPath("data[].stops[].lng").description("경도"),
                        fieldWithPath("data[].stops[].name").description("장소 이름"),
                        fieldWithPath("data[].stops[].sequence").description("일정 순서"),
                        fieldWithPath("data[].startDate").description("시작 일자"),
                        fieldWithPath("data[].endDate").description("종료 일자"),
                        fieldWithPath("data[].memo").description("메모")
                    )
                )
            )
    }
}
