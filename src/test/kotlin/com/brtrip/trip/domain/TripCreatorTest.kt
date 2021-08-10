package com.brtrip.trip.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.place.Place
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
        val request = TripRequest(
            title = "first trip",
            startDate = "2021-05-05",
            endDate = "2021-05-08",
            paths = listOf(
                PathRequest(
                    id = 1,
                    places = listOf(
                        Place(
                            lat = "123",
                            lng = "456",
                            name = "central park"
                        ),
                        Place(
                            lat = "789",
                            lng = "101",
                            name = "grand canyon"
                        )
                    )
                )
            )
        )

        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021,5,5),
            endDate = LocalDate.of(2021,5,8)
        )

        // when
        val result = sut.create(1L, request)

        // then
        result.title shouldBe trip.title
        result.userId shouldBe trip.userId
    }
}
