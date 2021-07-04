package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.place.Place
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
internal class TripDeleterTest {

    @Autowired
    private lateinit var sut: TripDeleter

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip"
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

        // when
        sut.delete(trip.id!!)
        val deleted = tripRepository.findById(trip.id!!)
            .orElseThrow { NotFoundException("여행 일정이 없습니다.") }

        // then
        deleted.deleted shouldBe true
        deleted.stops.forEach {
            it.deleted shouldBe true
        }
    }
}
