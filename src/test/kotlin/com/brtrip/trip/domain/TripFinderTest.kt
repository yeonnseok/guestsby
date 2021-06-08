package com.brtrip.trip.domain

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripFinderTest {

    @Autowired
    private lateinit var sut: TripFinder

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun `내 모든 여행 일정 조회`() {
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
                name = "central park",
                lat = 123,
                lng = 456,
                stoppedAt = LocalDateTime.of(2021,6,3,0,0,0),
                sequence = 1
            ),
            Stop(
                trip = trip,
                name = "grand canyon",
                lat = 789,
                lng = 101,
                stoppedAt = LocalDateTime.of(2021,6,4,0,0,0),
                sequence = 2
            )
        ))
        trip.stops = stops

        // when
        val trips = sut.findByUserId(1L)

        // then
        trips.size shouldBe 1
        trips[0].title shouldBe "first trip"
        trips[0].stops.size shouldBe 2
        trips[0].stops[0].name shouldBe "central park"
        trips[0].stops[1].name shouldBe "grand canyon"
    }

    @Test
    fun `내 최근 여행 일정 조회`() {
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
                name = "central park",
                lat = 123,
                lng = 456,
                stoppedAt = LocalDateTime.of(2021,6,3,0,0,0),
                sequence = 1
            ),
            Stop(
                trip = trip,
                name = "grand canyon",
                lat = 789,
                lng = 101,
                stoppedAt = LocalDateTime.of(2021,6,4,0,0,0),
                sequence = 2
            )
        ))
        trip.stops = stops

        // when
        val result = sut.findRecent(1L)

        // then
        result.title shouldBe "first trip"
        result.stops.size shouldBe 2
        result.stops[0].name shouldBe "central park"
        result.stops[1].name shouldBe "grand canyon"
    }
}
