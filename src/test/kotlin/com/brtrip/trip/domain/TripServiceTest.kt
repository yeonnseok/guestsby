package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.place.Place
import com.brtrip.trip.controller.request.StopRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
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
internal class TripServiceTest {

    @Autowired
    private lateinit var sut: TripService

    @Autowired
    private lateinit var tripFinder: TripFinder

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun `여행 일정 생성`() {
        // given
        val stopRequest = StopRequest(
            lat = BigDecimal(123),
            lng = BigDecimal(456),
            name = "central park"
        )

        val request = TripRequest(
            title = "first trip",
            stops = listOf(stopRequest),
            startDate = "2021-06-01",
            endDate = "2021-06-05"
        )

        // when
        val tripId = sut.create(1L, request)

        // then
        tripId shouldNotBe null
    }

    @Test
    fun `내 모든 여행 일정 조회`() {
        // given
        val trips = tripRepository.saveAll(listOf(
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
        ))
        trips[0].stops = mutableListOf(
            stopRepository.save(
                Stop(
                    trip = trips[0],
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                )
            ),
            stopRepository.save(
                Stop(
                    trip = trips[0],
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        trips[1].stops = mutableListOf(
            stopRepository.save(
                Stop(
                    trip = trips[1],
                    place = Place(
                        name = "rainbow cafe",
                        lat = BigDecimal(987),
                        lng = BigDecimal(654)
                    ),
                    sequence = 1
                )
            )
        )

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
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5)
            )
        )

        trip.stops = mutableListOf(
            stopRepository.save(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                )
            ),
            stopRepository.save(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        // when
        val result = sut.findRecentTrip(1L)

        // then
        result.title shouldBe "first trip"
        result.stops.size shouldBe 2
    }

    @Test
    fun `여행 일정 수정`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5),
                memo = "first trip"
            )
        )

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
        sut.update(1L, trip.id!!, request)
        val result = tripFinder.findById(trip.id!!)

        // then
        result.title shouldBe "new trip"
        result.startDate shouldBe LocalDate.of(2021,6,2)
        result.endDate shouldBe LocalDate.of(2021,6,6)
    }

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021, 6, 1),
                endDate = LocalDate.of(2021, 6, 5),
                memo = "first trip"
            )
        )

        // when
        sut.delete(1L, trip.id!!)

        // then
        shouldThrow<NotFoundException> { tripFinder.findById(trip.id!!) }
    }

    @Test
    fun `추천 경로 조회`() {
        // TODO
    }
}
