package com.guestsby.user.domain

import com.guestsby.TestDataLoader
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserFinderTest {

    @Autowired
    private lateinit var sut: UserFinder

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `유저 정보 조회`() {
        val user = testDataLoader.sample_user()

        // when
        val result = sut.findById(user.id!!)

        // then
        result.nickName shouldBe user.nickName
        result.email shouldBe user.email
        result.role shouldBe user.role
        result.authProvider shouldBe user.authProvider
    }
}
