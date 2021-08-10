package com.brtrip.trip.domain

import com.brtrip.TestDataLoader
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
internal class TripFinderTest {

    @Autowired
    private lateinit var sut: TripFinder

    @Autowired
    lateinit var testDataLoader: TestDataLoader

    @Test
    fun `내 모든 여행 일정 조회`() {
        // given
        testDataLoader.sample_trip_first(1L)

        // when
        val trips = sut.findByUserId(1L)

        // then
        trips.size shouldBe 1
        trips[0].title shouldBe "first trip"
    }

    @Test
    fun `내 최근 여행 일정 조회`() {
        // given
        testDataLoader.sample_trip_first(1L)

        // when
        val result = sut.findRecent(1L)

        // then
        result.title shouldBe "first trip"
    }
}
