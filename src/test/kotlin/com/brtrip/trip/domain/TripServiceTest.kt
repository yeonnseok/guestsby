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

    @BeforeEach
    fun setUp() {
        tripCreator = mockk(relaxed = true)
        sut = TripService(tripCreator)
    }

    @Test
    fun `여행 일정 생성`() {
        // given
        val stopRequest = StopCreateRequest(
            lat = 123,
            lng = 456,
            name = "central park",
            stoppedAt = "2021-06-03T00:00:00"
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
}
