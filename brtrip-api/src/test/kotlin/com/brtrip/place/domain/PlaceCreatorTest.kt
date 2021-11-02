package com.brtrip.place.domain

import com.brtrip.place.PlaceCreator
import com.brtrip.place.PlaceRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
class PlaceCreatorTest {
    @Autowired
    private lateinit var sut: PlaceCreator

    @Test
    fun `장소 생성`() {
        // given
        val placeRequest = PlaceRequest(lat = "123.123", lng = "456.456", "용두암", keywords = arrayOf("힐링"))

        // when
        val place = sut.create(placeRequest)

        // then
        place.lat shouldBe placeRequest.lat
        place.placeCategories[0].isRepresentative shouldBe true
    }
}