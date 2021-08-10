package com.brtrip.trip.domain

import com.brtrip.TestDataLoader
import com.brtrip.path.controller.request.PathRequest
import com.brtrip.place.Place
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripUpdaterTest {

    @Autowired
    private lateinit var sut: TripUpdater

    @Autowired
    private lateinit var tripFinder: TripFinder

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `여행 일정 수정`() {
        // given
        val trip = testDataLoader.sample_trip_first(1L)

        val request = TripRequest(
            title = "new trip",
            startDate = "2021-05-05",
            endDate = "2021-05-08",
            memo = null,
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

        // when
        sut.update(trip.id!!, request)
        val updated = tripFinder.findById(trip.id!!)

        // then
        updated.title shouldBe "new trip"
        updated.memo shouldBe null
    }
}