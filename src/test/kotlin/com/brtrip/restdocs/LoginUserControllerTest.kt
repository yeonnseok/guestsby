package com.brtrip.restdocs

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.auth.security.JwtTokenProvider
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class LoginUserControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    protected var token: String? = null

    protected var userId: Long? = null

    private val log = LoggerFactory.getLogger(javaClass)

    @BeforeEach
    fun setUp() {
        val user = userRepository.save(
                User(
                        nickName = "tester",
                        email = "test@test.com",
                        role = RoleType.ROLE_USER,
                        authProvider = AuthProvider.KAKAO
                )
        )
        userId = user.id

        val principal = UserPrincipal.of(user)
        val authentication = UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        token = jwtTokenProvider.createToken(authentication)
    }

    @Test
    fun getToken() {
        log.info("token : $token")

        token shouldNotBe null
    }
}
