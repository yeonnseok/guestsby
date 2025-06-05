package com.guestsby.trip.domain

import com.guestsby.path.controller.request.PathRequest
import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlace
import com.guestsby.path.domain.PathRepository
import com.guestsby.place.Place
import com.guestsby.place.PlaceRepository
import com.guestsby.place.PlaceRequest
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
internal class TripUpdaterTest {

    @Autowired
    private lateinit var sut: TripUpdater

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Test
    fun `여행일정 업데이트`() {
        // given
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2),
            PathPlace(path = path, place = place3, sequence = 3)
        )
        pathRepository.save(path)

        val path2 = Path()
        path2.pathPlaces = mutableListOf(
            PathPlace(path = path2, place = place1, sequence = 1),
            PathPlace(path = path2, place = place3, sequence = 2)
        )
        pathRepository.save(path2)

        val trip = Trip(title = "first trip", startDate = LocalDate.of(2021,7,21), endDate = LocalDate.of(2021,7,23), userId = 1)
        trip.tripPaths = mutableListOf(
            TripPath(trip = trip, path = path, sequence = 1),
            TripPath(trip = trip, path = path2, sequence = 2)
        )
        tripRepository.save(trip)

        val tripRequest = TripRequest(
            title = "change",
            startDate = "2021-08-21",
            endDate = "2021-08-23",
            memo = "null",
            paths = listOf(
                PathRequest(
                    id = path.id,
                    places = listOf(
                        PlaceRequest(lat = "54321.54321", lng = "12345.12345", name = "수정", keywords = arrayOf("수정"))
                    )
                )
            )
        )

        // when
        val updated = sut.update(trip.id!!, tripRequest)

        // then
        updated.tripPaths.size shouldBe 1
        updated.tripPaths.first().path.pathPlaces.size shouldBe 1
        updated.tripPaths.first().path.pathPlaces.first().place.name shouldBe "수정"
        updated.tripPaths.first().path.likeCount shouldBe 1
    }

    @Test
    fun `여행 일정 업데이트 - 장소 변경 없을 때`() {
        // given
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2),
            PathPlace(path = path, place = place3, sequence = 3)
        )
        pathRepository.save(path)

        val trip = Trip(title = "first trip", startDate = LocalDate.of(2021,7,21), endDate = LocalDate.of(2021,7,23), userId = 1)
        trip.tripPaths = mutableListOf(
            TripPath(trip = trip, path = path, sequence = 1)
        )
        tripRepository.save(trip)
        val tripRequest = TripRequest(
            title = "change",
            startDate = "2021-08-21",
            endDate = "2021-08-23",
            memo = "null",
            paths = listOf(
                PathRequest(
                    id = path.id,
                    places = listOf(
                        PlaceRequest(lat = "123.123", lng = "456.456", name = "용두암", keywords = arrayOf("힐링")),
                        PlaceRequest(lat = "789.789", lng = "321.321", name = "한라산 국립 공원", keywords = arrayOf("관광")),
                        PlaceRequest(lat = "000.000", lng = "000.000", name = "공항", keywords = arrayOf("주요시설"))
                    )
                )
            )
        )

        // when
        val updated = sut.update(trip.id!!, tripRequest)

        // then
        updated.tripPaths.first().path.likeCount shouldBe 0
    }
}
