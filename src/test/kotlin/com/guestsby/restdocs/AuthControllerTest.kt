package com.guestsby.restdocs

import com.guestsby.common.response.ResultType
import com.guestsby.user.domain.AuthProvider
import com.guestsby.user.domain.RoleType
import com.guestsby.user.domain.User
import com.guestsby.user.domain.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AuthControllerTest : ControllerTest() {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `Email 확인 API`() {
        // given
        val body = mapOf(
            "email" to "guestsby@com"
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/check")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.existed").value(false))
            .andDo(
                MockMvcRestDocumentation.document(
                    "auth/email",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Content-Type").description("전송 타입")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("email").description("이메일"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("result").description("응답 결과"),
                        PayloadDocumentation.fieldWithPath("statusCode").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("data.existed").description("이메일 중복 여부")
                    )
                )
            )
    }

    @Test
    fun `회원가입 API`() {
        // given
        val body = mapOf(
            "email" to "guestsby@com",
            "password" to "test123",
            "passwordCheck" to "test123",
            "role" to RoleType.ROLE_USER.name,
            "authProvider" to AuthProvider.GOOGLE.name
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.token").isNotEmpty)
            .andDo(
                MockMvcRestDocumentation.document(
                    "auth/signup",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Content-Type").description("전송 타입")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("email").description("이메일"),
                        PayloadDocumentation.fieldWithPath("password").description("비밀번호"),
                        PayloadDocumentation.fieldWithPath("passwordCheck").description("비밀번호 확인"),
                        PayloadDocumentation.fieldWithPath("role").description("권한"),
                        PayloadDocumentation.fieldWithPath("authProvider").description("Auth Provider")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("result").description("응답 결과"),
                        PayloadDocumentation.fieldWithPath("statusCode").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("data.token").description("JWT 토큰")
                    )
                )
            )
    }

    @Test
    fun `로그인 API`() {
        // given
        userRepository.save(
            User(
                nickName = "guestsby",
                email = "guestsby@com",
                password = passwordEncoder.encode("test123"),
                role = RoleType.ROLE_USER,
                authProvider = AuthProvider.LOCAL
            )
        )

        val body = mapOf(
            "email" to "guestsby@com",
            "password" to "test123"
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.token").isNotEmpty)
            .andDo(
                MockMvcRestDocumentation.document(
                    "auth/login",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Content-Type").description("전송 타입")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("email").description("이메일"),
                        PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("result").description("응답 결과"),
                        PayloadDocumentation.fieldWithPath("statusCode").description("상태 코드"),
                        PayloadDocumentation.fieldWithPath("data.token").description("JWT 토큰")
                    )
                )
            )
    }
}
