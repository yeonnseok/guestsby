package com.guestsby.user.domain

import com.guestsby.TestDataLoader
import com.guestsby.common.exceptions.NotFoundException
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserDeleterTest {

    @Autowired
    private lateinit var sut: UserDeleter

    @Autowired
    private lateinit var userFinder: UserFinder

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `유저 삭제`() {
        // given
        val user = testDataLoader.sample_user()

        // when
        sut.delete(user.id!!)

        // then
        shouldThrow<NotFoundException> { userFinder.findById(user.id!!) }
    }

}
