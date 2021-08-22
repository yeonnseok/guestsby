package com.brtrip.trip.domain

import com.brtrip.TestDataLoader
import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.domain.PathPlaceFinder
import com.brtrip.place.Place
import com.brtrip.place.PlaceRequest
import com.brtrip.trip.controller.request.TripRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripCreatorTest {

    @Autowired
    private lateinit var sut: TripCreator

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `여행 일정 저장 - 추천 받은 Path가 안 바뀌었을 경우`() {
        // given
        val place1 = Place(
            lat = "123",
            lng = "456",
            name = "central park"
        )

        val place2 = Place(
            lat = "789",
            lng = "101",
            name = "grand canyon"
        )

        val tripRequest = TripRequest(
            title = "first trip",
            startDate = "2021-08-05",
            endDate = "2021-08-08",
            paths = listOf(
                PathRequest(
                    id = 1,
                    places = listOf(
                        PlaceRequest(
                            lat = "123",
                            lng = "456",
                            name = "central park"
                        ),
                        PlaceRequest(
                            lat = "789",
                            lng = "101",
                            name = "grand canyon"
                        )
                    )
                )
            )
        )

        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021,8,5),
            endDate = LocalDate.of(2021,8,8)
        )

        // path 저장
        val path = testDataLoader.sample_path_first(1L)

        // place 저장
        testDataLoader.sample_place_first(place1)
        testDataLoader.sample_place_first(place2)

        // pathPlace 지정
        testDataLoader.sample_path_place_first(path, place1, 1)
        testDataLoader.sample_path_place_first(path, place2, 2)

        // when
        val createdTrip = sut.create(1L, tripRequest)

        // then
        createdTrip.title shouldBe trip.title
        createdTrip.userId shouldBe trip.userId
    }

    @Test
    fun `여행 일정 저장 - 추천 받은 Path를 바꿨을 경우`() {
        // given
        val place1 = Place(
            lat = "123",
            lng = "456",
            name = "central park"
        )

        val place2 = Place(
            lat = "789",
            lng = "101",
            name = "grand canyon"
        )

        val tripRequest = TripRequest(
            title = "first trip",
            startDate = "2021-08-05",
            endDate = "2021-08-08",
            paths = listOf(
                PathRequest(
                    id = 1,
                    places = listOf(
                        PlaceRequest(
                            lat = "789",
                            lng = "101",
                            name = "grand canyon"
                        ),
                        PlaceRequest(
                            lat = "123",
                            lng = "456",
                            name = "central park"
                        )
                    )
                )
            )
        )

        val trip = Trip(
            userId = 1L,
            title = "first trip",
            startDate = LocalDate.of(2021,8,5),
            endDate = LocalDate.of(2021,8,8)
        )

        // path 저장
        val path = testDataLoader.sample_path_first(1L)

        // place 저장
        testDataLoader.sample_place_first(place1)
        testDataLoader.sample_place_first(place2)

        // pathPlace 지정
        testDataLoader.sample_path_place_first(path, place1, 1)
        testDataLoader.sample_path_place_first(path, place2, 2)

        // when
        val createdTrip = sut.create(1L, tripRequest)

        // then
        createdTrip.title shouldBe trip.title
        createdTrip.userId shouldBe trip.userId
    }
}
