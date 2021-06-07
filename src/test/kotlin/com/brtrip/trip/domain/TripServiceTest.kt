package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.StopCreateRequest
import com.brtrip.trip.controller.request.TripCreateRequest
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class TripServiceTest {

    private lateinit var sut: TripService

    private lateinit var tripCreator: TripCreator

    private lateinit var tripFinder: TripFinder

    @BeforeEach
    fun setUp() {
        tripCreator = mockk(relaxed = true)
        tripFinder = mockk(relaxed = true)
        sut = TripService(tripCreator, tripFinder)
    }

    @Test
    fun `여행 일정 생성`() {
        // given
        val stopRequest = StopCreateRequest(
            lat = 123,
            lng = 456,
            name = "central park",
            stoppedAt = "2021-06-03 00:00:00"
        )

        val request = TripCreateRequest(
            title = "first trip",
            stops = listOf(stopRequest),
            startDate = "2021-06-01",
            endDate = "2021-06-05"
        )

        val trip = Trip(
            id = 1L,
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021, 6, 1),
            endDate = LocalDate.of(2021, 6, 5)
        )

        every { tripCreator.create(any(), any()) } returns trip

        // when
        val tripId = sut.create(1L, request)

        // then
        tripId shouldBe 1L
    }

    @Test
    fun `내 모든 여행 일정 조회`() {
        // given
        val trips = listOf(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            ),
            Trip(
                userId = 1L,
                title = "second trip",
                startDate = LocalDate.of(2021, 8, 1),
                endDate = LocalDate.of(2021, 8, 1)
            )
        )
        trips[0].stops = listOf(
            Stop(
                trip = trips[0],
                name = "central park",
                lat = 123,
                lng = 456,
                stoppedAt = LocalDateTime.of(2021,6,3,0,0,0),
                sequence = 1
            ),
            Stop(
                trip = trips[0],
                name = "grand canyon",
                lat = 789,
                lng = 101,
                stoppedAt = LocalDateTime.of(2021,6,4,0,0,0),
                sequence = 2
            )
        )

        trips[1].stops = listOf(
            Stop(
                trip = trips[1],
                name = "rainbow cafe",
                lat = 987,
                lng = 654,
                stoppedAt = LocalDateTime.of(2021,8,1,0,0,0),
                sequence = 1
            )
        )

        every { tripFinder.findByUserId(any()) } returns trips

        // when
        val myTrips = sut.findMyTrips(1L)

        // then
        myTrips.size shouldBe 2
        myTrips[0].title shouldBe "first trip"
        myTrips[0].stops.size shouldBe 2

        myTrips[1].title shouldBe "second trip"
        myTrips[1].stops.size shouldBe 1
    }

    @Test
    fun `내 최근 여행 일정 조회`() {
        // given
        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021, 6, 1),
            endDate = LocalDate.of(2021, 6, 5)
        )

        trip.stops = listOf(
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
        )

        every { tripFinder.findRecent(any()) } returns trip

        // when
        val result = sut.findRecentTrip(1L)

        // then
        result.title shouldBe "first trip"
        result.stops.size shouldBe 2
    }
}
