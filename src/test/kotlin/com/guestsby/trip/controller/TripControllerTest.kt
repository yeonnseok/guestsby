package com.guestsby.trip.controller

import com.guestsby.common.response.ResultType
import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlace
import com.guestsby.path.domain.PathRepository
import com.guestsby.place.Category
import com.guestsby.place.Place
import com.guestsby.place.PlaceCategory
import com.guestsby.place.PlaceRepository
import com.guestsby.restdocs.LoginUserControllerTest
import com.guestsby.trip.domain.Trip
import com.guestsby.trip.domain.TripPath
import com.guestsby.trip.domain.TripRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class TripControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Test
    fun `여행 일정 생성 API`() {
        // given
        // 1번 Place
        val place1 = Place(
            lat = "123.123", lng = "456.456", name = "용두암"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        // 2번 Place
        val place2 = Place(
            lat = "789.789", lng = "321.321", name = "한라산 국립 공원"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        pathRepository.save(path)

        val body = mapOf(
            "title" to "first trip",
            "startDate" to "2021-08-10",
            "endDate" to "2021-08-12",
            "memo" to "test",
            "paths" to listOf(
                mapOf(
                    "id" to path.id,
                    "places" to listOf(
                        mapOf(
                            "lat" to "123.123",
                            "lng" to "456.456",
                            "name" to "용두암",
                            "keywords" to arrayOf("힐링")
                        ),
                        mapOf(
                            "lat" to "789.789",
                            "lng" to "321.321",
                            "name" to "한라산 국립 공원",
                            "keywords" to arrayOf("관광")
                        )
                    )
                )
            )
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
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("data.tripId").isNotEmpty)
            .andDo(
                document(
                    "trip/create",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                        headerWithName("Content-Type").description("컨텐츠 타입")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.tripId").description("일정 ID")
                    )
                )
            )
    }

    @Test
    fun `여행 일정 수정 API`() {
        // given
        val place1 = Place(
            lat = "123.123", lng = "456.456", name = "용두암"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        val place2 = Place(
            lat = "789.789", lng = "321.321", name = "한라산 국립 공원"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        val place3 = Place(
            lat = "000.000", lng = "000.000", name = "공항"
        )
        place3.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "주요시설"
                ),
                place3,
                false
            )
        )
        val savedPlace3 = placeRepository.save(place3)

        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2),
            PathPlace(path = path, place = savedPlace3, sequence = 3)
        )
        pathRepository.save(path)

        val anotherPath = Path(likeCount = 0)
        anotherPath.pathPlaces = mutableListOf(
            PathPlace(path = anotherPath, place = savedPlace1, sequence = 1),
            PathPlace(path = anotherPath, place = savedPlace3, sequence = 2),
            PathPlace(path = anotherPath, place = savedPlace2, sequence = 3)
        )
        pathRepository.save(anotherPath)

        val trip = Trip(
            userId = userId!!,
            title = "first trip",
            startDate = LocalDate.of(2021,8,11),
            endDate = LocalDate.of(2021,8,13)
        )
        trip.tripPaths = mutableListOf(TripPath(trip = trip, path = path, sequence = 1))
        tripRepository.save(trip)

        val body = mapOf(
            "title" to "수정된 일정",
            "startDate" to "2021-08-10",
            "endDate" to "2021-08-12",
            "memo" to "test",
            "paths" to listOf(
                mapOf(
                    "id" to anotherPath.id,
                    "places" to listOf(
                        mapOf(
                            "lat" to "123.123",
                            "lng" to "456.456",
                            "name" to "용두암",
                            "keywords" to arrayOf("힐링")
                        ),
                        mapOf(
                            "lat" to "000.000",
                            "lng" to "000.000",
                            "name" to "공항",
                            "keywords" to arrayOf("주요시설")
                        ),
                        mapOf(
                            "lat" to "789.789",
                            "lng" to "321.321",
                            "name" to "한라산 국립 공원",
                            "keywords" to arrayOf("관광")
                        )
                    )
                )
            )
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
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.title").value("수정된 일정"))
            .andDo(
                document(
                    "trip/update",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                        headerWithName("Content-Type").description("컨텐츠 타입")
                    ),
                    pathParameters(
                       parameterWithName("id").description("여행일정 ID")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.id").description("일정 id"),
                        fieldWithPath("data.title").description("일정 제목"),
                        fieldWithPath("data.startDate").description("시작 일자"),
                        fieldWithPath("data.endDate").description("종료 일자"),
                        fieldWithPath("data.memo").description("메모"),
                        fieldWithPath("data.paths[].id").description("경로 id"),
                        fieldWithPath("data.paths[].name").description("경로 이름"),
                        fieldWithPath("data.paths[].likeCount").description("경로 좋아요 수"),
                        fieldWithPath("data.paths[].places[].lat").description("경도"),
                        fieldWithPath("data.paths[].places[].lng").description("위도"),
                        fieldWithPath("data.paths[].places[].name").description("장소 이름"),
                        fieldWithPath("data.paths[].places[].keywords").description("카테고리")
                    )
                )
            )
    }

    @Test
    fun `여행 일정 조회 API`() {
        // given
        // 1번 Place
        val place1 = Place(
            lat = "123.123", lng = "456.456", name = "용두암"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        // 2번 Place
        val place2 = Place(
            lat = "789.789", lng = "321.321", name = "한라산 국립 공원"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)
//        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
//        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        pathRepository.save(path)

        val trip = Trip(
            userId = userId!!,
            title = "first trip",
            startDate = LocalDate.of(2021,8,11),
            endDate = LocalDate.of(2021,8,13)
        )
        trip.tripPaths = mutableListOf(TripPath(trip = trip, path = path, sequence = 1))
        tripRepository.save(trip)

        // when
        val result = mockMvc.perform(
            get("/api/v1/trips/my")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andDo(
                document(
                    "trip/my",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data[].id").description("일정 id"),
                        fieldWithPath("data[].title").description("일정 제목"),
                        fieldWithPath("data[].startDate").description("시작 일자"),
                        fieldWithPath("data[].endDate").description("종료 일자"),
                        fieldWithPath("data[].memo").description("메모"),
                        fieldWithPath("data[].paths[].id").description("경로 id"),
                        fieldWithPath("data[].paths[].name").description("경로 이름"),
                        fieldWithPath("data[].paths[].likeCount").description("경로 좋아요 수"),
                        fieldWithPath("data[].paths[].places[].lat").description("경도"),
                        fieldWithPath("data[].paths[].places[].lng").description("위도"),
                        fieldWithPath("data[].paths[].places[].name").description("장소 이름"),
                        fieldWithPath("data[].paths[].places[].keywords").description("카테고리")
                    )
                )
            )
    }

    @Test
    fun `여행 일정 삭제 API`() {
        // given
        // 1번 Place
        val place1 = Place(
            lat = "123.123", lng = "456.456", name = "용두암"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        // 2번 Place
        val place2 = Place(
            lat = "789.789", lng = "321.321", name = "한라산 국립 공원"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        pathRepository.save(path)

        val trip = Trip(
            userId = userId!!,
            title = "first trip",
            startDate = LocalDate.of(2021,8,11),
            endDate = LocalDate.of(2021,8,13)
        )
        trip.tripPaths = mutableListOf(TripPath(trip = trip, path = path, sequence = 1))
        tripRepository.save(trip)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/v1/trips/{id}", trip.id)
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data").isEmpty)
            .andDo(
                document(
                    "trip/delete",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    pathParameters(
                        parameterWithName("id").description("여행일정 ID")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data").description("데이터")
                    )
                )
            )
    }
}
