package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.StopRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDate

@SpringBootTest
@Sql("/truncate.sql")
internal class TripCreatorTest {

    @Autowired
    private lateinit var sut: TripCreator

    @Test
    fun `여행 일정 저장`() {
        // given
        val stopRequest = StopRequest(
            lat = 123,
            lng = 456,
            name = "central park"
        )

        val request = TripRequest(
            title = "first trip",
            stops = listOf(stopRequest),
            startDate = "2021-06-01",
            endDate = "2021-06-05"
        )

        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021, 6, 1),
            endDate = LocalDate.of(2021, 6, 5)
        )

        // when
        val result = sut.create(1L, request)

        // then
        result.title shouldBe trip.title
        result.userId shouldBe trip.userId

        result.stops.size shouldBe 1
        result.stops[0].trip.title shouldBe trip.title
    }
}
