//package com.guestsby.favorite.domain
//
//import com.guestsby.TestDataLoader
//import com.guestsby.common.exceptions.NotFoundException
//import com.guestsby.favorite.domain.FavoriteDeleter
//import com.guestsby.favorite.domain.FavoriteRepository
//import com.guestsby.path.domain.Path
//import com.guestsby.path.domain.PathPlace
//import com.guestsby.path.domain.PathRepository
//import com.guestsby.place.Place
//import com.guestsby.place.PlaceRepository
//import com.guestsby.trip.domain.Trip
//import com.guestsby.trip.domain.TripPath
//import com.guestsby.trip.domain.TripRepository
//import com.guestsby.user.domain.AuthProvider
//import com.guestsby.user.domain.RoleType
//import com.guestsby.user.domain.User
//import com.guestsby.user.domain.UserRepository
//import io.kotlintest.shouldBe
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.jdbc.Sql
//import org.springframework.transaction.annotation.Transactional
//import java.time.LocalDate
//
//@SpringBootTest
//@Transactional
//@Sql("/truncate.sql")
//internal class FavoriteDeleterTest {
//
//    @Autowired
//    private lateinit var sut: FavoriteDeleter
//
//    @Autowired
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var favoriteRepository: FavoriteRepository
//
//    @Autowired
//    private lateinit var placeRepository: PlaceRepository
//
//    @Autowired
//    private lateinit var pathRepository: PathRepository
//
//    @Autowired
//    private lateinit var tripRepository: TripRepository
//
//    @Test
//    fun `찜 삭제 api`() {
//        // given
//        // place 저장
//        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
//        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
//        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
//
//        // path(+pathPlace) 저장
//        val path = Path()
//        path.pathPlaces = mutableListOf(
//            PathPlace(path = path, place = place1, sequence = 1),
//            PathPlace(path = path, place = place2, sequence = 2),
//            PathPlace(path = path, place = place3, sequence = 3)
//        )
//        pathRepository.save(path)
//
//        val path2 = Path()
//        path2.pathPlaces = mutableListOf(
//            PathPlace(path = path2, place = place1, sequence = 1),
//            PathPlace(path = path2, place = place3, sequence = 2)
//        )
//        pathRepository.save(path2)
//
//        // trip(+tripPath) 저장
//        val trip = Trip(title = "first trip", startDate = LocalDate.of(2021,7,21), endDate = LocalDate.of(2021,7,23), userId = 1)
//        trip.tripPaths = mutableListOf(
//            TripPath(trip = trip, path = path, sequence = 1),
//            TripPath(trip = trip, path = path2, sequence = 2)
//        )
//        tripRepository.save(trip)
//
//        // user(+favorite) 저장
//        val user = User(
//            id = 2L,
//            nickName = "여행가",
//            email = "trip@com",
//            password = "test123",
//            role = RoleType.ROLE_USER,
//            authProvider = AuthProvider.KAKAO
//        )
//        user.favorites = mutableListOf(
//            Favorite(user = user, path = path)
//        )
//        userRepository.save(user)
//
//        // when
//        sut.delete(1L)
//        val deleted = favoriteRepository.findById(1L)
//            .orElseThrow { NotFoundException("찜 목록이 없습니다.") }
//
//        // then
//        deleted.deleted shouldBe true
//    }
//}
