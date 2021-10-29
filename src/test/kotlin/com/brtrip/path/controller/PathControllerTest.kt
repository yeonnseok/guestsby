package com.brtrip.path.controller

import com.brtrip.common.response.ResultType
import com.brtrip.path.domain.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Category
import com.brtrip.place.Place
import com.brtrip.place.PlaceCategory
import com.brtrip.place.PlaceRepository
import com.brtrip.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@Transactional
class PathControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Test
    fun `여행 경로 추천`() {
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

        // 3번 Place
        val place3 = Place(
            lat = "131.131", lng = "454.454", name = "카페"
        )
        place3.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "일반시설"
                ),
                place3,
                false
            )
        )
        val savedPlace3 = placeRepository.save(place3)

        // 4번 Place
        val place4 = Place(
            lat = "787.787", lng = "323.323", name = "해변"
        )
        place4.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place4,
                false
            )
        )
        val savedPlace4 = placeRepository.save(place4)

        // 5번 Place
        val place5 = Place(
            lat = "999.999", lng = "888.888", name = "절벽"
        )
        place5.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "휴식"
                ),
                place5,
                false
            )
        )
        val savedPlace5 = placeRepository.save(place5)

        // 경로 저장
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        path.name = "경로1"
        pathRepository.save(path)

        val path2 = Path(likeCount = 0)
        path2.pathPlaces = mutableListOf(
            PathPlace(path = path2, place = savedPlace2, sequence = 1),
            PathPlace(path = path2, place = savedPlace3, sequence = 2),
            PathPlace(path = path2, place = savedPlace4, sequence = 3)
        )
        path2.name = "경로2"
        pathRepository.save(path2)

        val path3 = Path(likeCount = 0)
        path3.pathPlaces = mutableListOf(
            PathPlace(path = path3, place = savedPlace1, sequence = 1),
            PathPlace(path = path3, place = savedPlace2, sequence = 2),
            PathPlace(path = path3, place = savedPlace4, sequence = 3)
        )
        path3.name = "경로3"
        pathRepository.save(path3)

        val path4 = Path(likeCount = 0)
        path4.pathPlaces = mutableListOf(
            PathPlace(path = path4, place = savedPlace3, sequence = 1),
            PathPlace(path = path4, place = savedPlace2, sequence = 2),
            PathPlace(path = path4, place = savedPlace1, sequence = 3)
        )
        path4.name = "경로4"
        pathRepository.save(path4)

        val path5 = Path(likeCount = 0)
        path5.pathPlaces = mutableListOf(
            PathPlace(path = path5, place = savedPlace5, sequence = 1),
            PathPlace(path = path5, place = savedPlace2, sequence = 2),
            PathPlace(path = path5, place = savedPlace3, sequence = 3),
            PathPlace(path = path5, place = savedPlace4, sequence = 4),
        )
        path5.name = "경로5"
        pathRepository.save(path5)

        val path6 = Path(likeCount = 0)
        path6.pathPlaces = mutableListOf(
            PathPlace(path = path6, place = savedPlace2, sequence = 1),
            PathPlace(path = path6, place = savedPlace4, sequence = 2),
            PathPlace(path = path6, place = savedPlace1, sequence = 3),
            PathPlace(path = path6, place = savedPlace5, sequence = 4)
        )
        path6.name = "경로6"
        pathRepository.save(path6)

        // when
        val result = mockMvc.perform(
            get("/api/v1/paths/recommend?lat=789.789&lng=321.321")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andDo(
                document(
                    "path/recommend",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestParameters(
                      parameterWithName("lat").description("위도"),
                      parameterWithName("lng").description("경도")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data[].id").description("경로 id"),
                        fieldWithPath("data[].name").description("경로 이름"),
                        fieldWithPath("data[].likeCount").description("경로 좋아요 수"),
                        fieldWithPath("data[].places[].lat").description("경도"),
                        fieldWithPath("data[].places[].lng").description("위도"),
                        fieldWithPath("data[].places[].name").description("장소 이름"),
                        fieldWithPath("data[].places[].keywords").description("카테고리")
                    )
                )
            )
    }
}
