package com.brtrip.path.controller

import com.brtrip.common.response.ResultType
import com.brtrip.path.domain.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Place
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
    fun `여행 경로 추천 API`() {
        // given
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2)
        )
        pathRepository.save(path)

        val place3 = placeRepository.save(Place(lat = "131.131", lng = "454.454", name = "카페"))
        val place4 = placeRepository.save(Place(lat = "787.787", lng = "323.323", name = "해변"))
        val path2 = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path2, place = place2, sequence = 1),
            PathPlace(path = path2, place = place3, sequence = 2),
            PathPlace(path = path2, place = place4, sequence = 3)
        )
        pathRepository.save(path2)

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
                        fieldWithPath("data[].name").description("경로 이름"),
                        fieldWithPath("data[].likeCount").description("경로 좋아요 수"),
                        fieldWithPath("data[].places[].lat").description("경도"),
                        fieldWithPath("data[].places[].lng").description("위도"),
                        fieldWithPath("data[].places[].name").description("장소 이름")
                    )
                )
            )
    }
}
