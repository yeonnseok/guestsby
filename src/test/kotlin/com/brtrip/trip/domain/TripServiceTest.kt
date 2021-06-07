package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.StopRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    private lateinit var tripUpdater: TripUpdater

    private lateinit var tripDeleter: TripDeleter

    @BeforeEach
    fun setUp() {
        tripCreator = mockk(relaxed = true)
        tripFinder = mockk(relaxed = true)
        tripUpdater = mockk(relaxed = true)
        tripDeleter = mockk(relaxed = true)

        sut = TripService(tripCreator, tripFinder, tripUpdater, tripDeleter)
    }

    @Test
    fun `여행 일정 생성`() {
        // given
        val stopRequest = StopRequest(
            lat = 123,
            lng = 456,
            name = "central park",
            stoppedAt = "2021-06-03 00:00:00"
        )

        val request = TripRequest(
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
        trips[0].stops = mutableListOf(
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

        trips[1].stops = mutableListOf(
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

        trip.stops = mutableListOf(
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

    @Test
    fun `여행 일정 수정`() {
        // given
        val request = TripRequest(
            title = "new trip",
            stops = listOf(
                StopRequest(
                   lat = 123,
                   lng = 456,
                   name = "grand canyon",
                   stoppedAt = "2021-06-04 00:00:00"
                ),
                StopRequest(
                    lat = 789,
                    lng = 101,
                    name = "rainbow cafe",
                    stoppedAt = "2021-06-05 00:00:00"
                )
            ),
            startDate = "2021-06-02",
            endDate = "2021-06-06",
            memo = null
        )

        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021,6,1),
            endDate = LocalDate.of(2021,6,5),
            memo = "first trip"
        )

        every { tripFinder.findById(any()) } returns trip

        // when
        sut.update(1L, 1L, request)

        // then
        verify { tripFinder.findById(1L) }
        verify { tripUpdater.update(1L, request) }
    }

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021,6,1),
            endDate = LocalDate.of(2021,6,5),
            memo = "first trip"
        )

        every { tripFinder.findById(any()) } returns trip

        // when
        sut.delete(1L, 1L)

        // then
        verify { tripFinder.findById(1L) }
        verify { tripDeleter.delete(1L) }
    }
}
