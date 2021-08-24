package com.brtrip.favorite.domain

import com.brtrip.TestDataLoader
import com.brtrip.path.domain.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripPath
import com.brtrip.trip.domain.TripRepository
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
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
internal class FavoriteFinderTest {

    @Autowired
    private lateinit var sut: FavoriteFinder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Test
    fun `찜한 여행 목록 불러오기`() {
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

        // user(+favorite) 저장
        val user = User(
            id = 1L,
            nickName = "여행가",
            email = "trip@com",
            password = "test123",
            role = RoleType.ROLE_USER,
            authProvider = AuthProvider.KAKAO
        )
        user.favorites = mutableListOf(
            Favorite(id = 1L, user = user, path = path)
        )
        userRepository.save(user)

        // when
        val result = sut.findByUser(user)

        // then
        result[0].user.id shouldBe user.id
        result[0].user.nickName shouldBe user.nickName
        result[0].user.email shouldBe user.email
        result[0].user.role shouldBe user.role
        result[0].user.authProvider shouldBe user.authProvider

        result[0].path.id shouldBe path.id

        result[0].path.pathPlaces[0].id shouldBe path.pathPlaces[0].id
        result[0].path.pathPlaces[0].place shouldBe path.pathPlaces[0].place
        result[0].path.pathPlaces[0].sequence shouldBe path.pathPlaces[0].sequence

        result[0].path.pathPlaces[1].id shouldBe path.pathPlaces[1].id
        result[0].path.pathPlaces[1].place shouldBe path.pathPlaces[1].place
        result[0].path.pathPlaces[1].sequence shouldBe path.pathPlaces[1].sequence

        result[0].path.pathPlaces[2].id shouldBe path.pathPlaces[2].id
        result[0].path.pathPlaces[2].place shouldBe path.pathPlaces[2].place
        result[0].path.pathPlaces[2].sequence shouldBe path.pathPlaces[2].sequence
    }
}