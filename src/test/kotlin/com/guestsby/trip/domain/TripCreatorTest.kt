package com.guestsby.trip.domain

import com.guestsby.path.controller.request.PathRequest
import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlace
import com.guestsby.path.domain.PathRepository
import com.guestsby.place.*
import com.guestsby.trip.controller.request.TripRequest
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
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Test
    fun `여행 일정 저장 - 추천 받은 Path가 안 바뀌었을 경우`() {
        // given
        // 1번 Place
        val place1 = Place(
            lat = "123",
            lng = "456",
            name = "central park"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            ),
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        // 2번 Place
        val place2 = Place(
            lat = "789",
            lng = "101",
            name = "grand canyon"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "명승지"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        // path
        val path = Path(null, 1)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        pathRepository.save(path)

        // tripRequest
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
                            name = "central park",
                            keywords = arrayOf("힐링", "관광")
                        ),
                        PlaceRequest(
                            lat = "789",
                            lng = "101",
                            name = "grand canyon",
                            keywords = arrayOf("명승지")
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

        // when
        val createdTrip = sut.create(1L, tripRequest)

        // then
        createdTrip.title shouldBe trip.title
        createdTrip.userId shouldBe trip.userId
        createdTrip.tripPaths[0].path.pathPlaces[0].place.placeCategories[0].category.name shouldBe
                place1.placeCategories[0].category.name // category check
    }

    @Test
    fun `여행 일정 저장 - 추천 받은 Path를 바꿨을 경우`() {
        // given
        // 1번 Place
        val place1 = Place(
            lat = "123",
            lng = "456",
            name = "central park"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            ),
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        // 2번 Place
        val place2 = Place(
            lat = "789",
            lng = "101",
            name = "grand canyon"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "명승지"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        // path
        val path = Path(null, 1)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        pathRepository.save(path)

        // tripRequest
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
                            name = "grand canyon",
                            keywords = arrayOf("명승지")
                        ),
                        PlaceRequest(
                            lat = "123",
                            lng = "456",
                            name = "central park",
                            keywords = arrayOf("힐링", "관광")
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

        // when
        val createdTrip = sut.create(1L, tripRequest)

        // then
        createdTrip.title shouldBe trip.title
        createdTrip.userId shouldBe trip.userId
        createdTrip.tripPaths[0].path.pathPlaces[0].place.placeCategories[0].category.name shouldBe
                place1.placeCategories[0].category.name // category check
    }
}
