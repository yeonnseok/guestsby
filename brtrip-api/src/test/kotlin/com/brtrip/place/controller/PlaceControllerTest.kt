package com.brtrip.place.controller

import com.brtrip.common.response.ResultType
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.restdocs.ControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class PlaceControllerTest : ControllerTest() {

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Test
    fun `여행 경로 추천 API`() {
        // given
        val place = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/places/{id}", place.id)
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andDo(
                document(
                    "place/find",
                    pathParameters(
                        parameterWithName("id").description("Place ID")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.lat").description("경도"),
                        fieldWithPath("data.lng").description("위도"),
                        fieldWithPath("data.name").description("장소 이름"),
                        fieldWithPath("data.content").description("설명")
                    )
                )
            )
    }
}
