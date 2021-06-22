package com.brtrip.trip.domain

import com.brtrip.place.Place
import com.brtrip.trip.controller.request.StopRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripUpdaterTest {

    @Autowired
    private lateinit var sut: TripUpdater

    @Autowired
    private lateinit var tripFinder: TripFinder

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun `여행 일정 수정`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021,6,1),
                endDate = LocalDate.of(2021,6,5)
            )
        )

        val stops = stopRepository.saveAll(listOf(
            Stop(
                trip = trip,
                place = Place(
                    name = "central park",
                    lat = BigDecimal(123),
                    lng = BigDecimal(456)
                ),
                sequence = 1
            ),
            Stop(
                trip = trip,
                place = Place(
                    name = "grand canyon",
                    lat = BigDecimal(789),
                    lng = BigDecimal(101)
                ),
                sequence = 2
            )
        ))
        trip.stops = stops

        val request = TripRequest(
            title = "new trip",
            stops = listOf(
                StopRequest(
                    lat = BigDecimal(123),
                    lng = BigDecimal(456),
                    name = "grand canyon"
                ),
                StopRequest(
                    lat = BigDecimal(789),
                    lng = BigDecimal(101),
                    name = "rainbow cafe"
                )
            ),
            startDate = "2021-06-02",
            endDate = "2021-06-06",
            memo = null
        )

        // when
        sut.update(trip.id!!, request)
        val updated = tripFinder.findById(trip.id!!)

        // then
        updated.title shouldBe "new trip"
        updated.startDate shouldBe LocalDate.of(2021,6,2)
        updated.endDate shouldBe LocalDate.of(2021,6,6)
        updated.memo shouldBe null

        updated.stops.size shouldBe 2
        updated.stops[0].sequence shouldBe 1
        updated.stops[0].place.name shouldBe "grand canyon"
        updated.stops[1].sequence shouldBe 2
        updated.stops[1].place.name shouldBe "rainbow cafe"
    }
}