package com.brtrip.trip.domain

import com.brtrip.TestDataLoader
import com.brtrip.common.exceptions.NotFoundException
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripDeletorTest {

    @Autowired
    private lateinit var sut: TripDeleter

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = testDataLoader.sample_trip_first(1L)
        val path = testDataLoader.sample_path_first(1L)
        testDataLoader.sample_trip_path_first(trip, path)

        // when
        sut.delete(trip.id!!)
        val deleted = tripRepository.findById(trip.id!!)
            .orElseThrow { NotFoundException("여행 일정이 없습니다.") }

        // then
        deleted.deleted shouldBe true
        deleted.tripPaths.forEach {
            it.deleted shouldBe true
        }
    }
}