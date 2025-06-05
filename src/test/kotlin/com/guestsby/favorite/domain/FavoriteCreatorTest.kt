package com.guestsby.favorite.domain

import com.guestsby.TestDataLoader
import com.guestsby.favorite.controller.request.FavoriteRequest
import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlace
import com.guestsby.path.domain.PathRepository
import com.guestsby.place.Place
import com.guestsby.place.PlaceRepository
import com.guestsby.trip.domain.Trip
import com.guestsby.trip.domain.TripPath
import com.guestsby.trip.domain.TripRepository
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
class FavoriteCreatorTest {
    @Autowired
    private lateinit var sut: FavoriteCreator

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `찜 저장하기`() {
        // given
        // place 저장
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))

        // path(+pathPlace) 저장
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

        // trip(+tripPath) 저장
        val trip = Trip(title = "first trip", startDate = LocalDate.of(2021,7,21), endDate = LocalDate.of(2021,7,23), userId = 1)
        trip.tripPaths = mutableListOf(
            TripPath(trip = trip, path = path, sequence = 1),
            TripPath(trip = trip, path = path2, sequence = 2)
        )
        tripRepository.save(trip)

        // user 저장
        val user = testDataLoader.sample_user()

        // request
        val favoriteRequest = FavoriteRequest(
            pathId = path.id!!
        )

        // when
        val result = sut.create(user.id!!, favoriteRequest)

        // then
        result.user.id shouldBe user.id
        result.user.nickName shouldBe user.nickName
        result.user.email shouldBe user.email
        result.user.role shouldBe user.role
        result.user.authProvider shouldBe user.authProvider

        result.path.id shouldBe path.id

        result.path.pathPlaces[0].id shouldBe path.pathPlaces[0].id
        result.path.pathPlaces[0].place shouldBe path.pathPlaces[0].place
        result.path.pathPlaces[0].sequence shouldBe path.pathPlaces[0].sequence

        result.path.pathPlaces[1].id shouldBe path.pathPlaces[1].id
        result.path.pathPlaces[1].place shouldBe path.pathPlaces[1].place
        result.path.pathPlaces[1].sequence shouldBe path.pathPlaces[1].sequence

        result.path.pathPlaces[2].id shouldBe path.pathPlaces[2].id
        result.path.pathPlaces[2].place shouldBe path.pathPlaces[2].place
        result.path.pathPlaces[2].sequence shouldBe path.pathPlaces[2].sequence
    }
}
