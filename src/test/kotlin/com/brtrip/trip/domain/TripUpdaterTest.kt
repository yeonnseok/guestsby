package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.StopRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Assertions.*
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

        // when
        sut.update(trip.id!!, request)
        val updated = tripFinder.findById(trip.id!!)

        // then
        updated.title shouldBe "new trip"
        updated.startDate shouldBe LocalDate.of(2021,6,2)
        updated.endDate shouldBe LocalDate.of(2021,6,6)
        updated.memo shouldBe null

        updated.stops!!.size shouldBe 2
        updated.stops!![0].sequence shouldBe 1
        updated.stops!![0].name shouldBe "grand canyon"
        updated.stops!![1].sequence shouldBe 2
        updated.stops!![1].name shouldBe "rainbow cafe"
    }
}