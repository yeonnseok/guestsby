package com.guestsby.user.controller

import com.guestsby.common.response.ResultType
import com.guestsby.restdocs.LoginUserControllerTest
import com.guestsby.user.domain.RoleType
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest : LoginUserControllerTest() {

    @Test
    fun `로그인 유저 조회 API`() {
        // when
        val result = mockMvc.perform(
            get("/api/v1/users/me")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.nickName").value("tester"))
            .andExpect(jsonPath("data.email").value("test@test.com"))
            .andExpect(jsonPath("data.role").value(RoleType.ROLE_USER.text))
            .andDo(
                document(
                    "users/me",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.nickName").description("닉네임"),
                        fieldWithPath("data.email").description("이메일"),
                        fieldWithPath("data.role").description("권한")
                    )
                )
            )
    }

    @Test
    fun `유저 닉네임 수정 API`() {
        // given
        val body = mapOf(
            "nickName" to "여행가"
        )

        // when
        val result = mockMvc.perform(
            patch("/api/v1/users/me")
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
                    "users/update",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestFields(
                        fieldWithPath("nickName").description("닉네임")
                    )
                )
            )
    }

    @Test
    fun `회원 탈퇴 API`() {
        // when
        val result = mockMvc.perform(
            delete("/api/v1/users/me")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // then
        result
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "users/delete",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    )
                )
            )
    }
}
