package com.brtrip.place

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class PlaceFinderTest {

    @Autowired
    private lateinit var sut: PlaceFinder

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Test
    fun `좌표로 장소 찾기`() {
        // given
        placeRepository.save(
            Place(
                lat = "123.123",
                lng = "456.456",
                name = "central park"
            )
        )

        // when
        val place = sut.findByPosition("123.123", "456.456")

        // then
        place.lat shouldBe "123.123"
        place.lng shouldBe "456.456"
    }
}