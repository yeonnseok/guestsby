package com.guestsby.common

import com.guestsby.path.domain.Path
import com.guestsby.path.domain.PathPlace
import com.guestsby.path.domain.PathRepository
import com.guestsby.place.Category
import com.guestsby.place.Place
import com.guestsby.place.PlaceCategory
import com.guestsby.place.PlaceRepository
import com.guestsby.trip.domain.Trip
import com.guestsby.trip.domain.TripPath
import com.guestsby.trip.domain.TripRepository
import com.guestsby.user.domain.AuthProvider
import com.guestsby.user.domain.RoleType
import com.guestsby.user.domain.User
import com.guestsby.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataLoader(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository,
    private val pathRepository: PathRepository,
    private val tripRepository: TripRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        // User
        val user = userRepository.save(
            User(
                nickName = "admin",
                email = "test@com",
                password = passwordEncoder.encode("test"),
                role = RoleType.ROLE_ADMIN,
                authProvider = AuthProvider.LOCAL
            )
        )

        // Place
        val place1 = Place(lat = "33.5161106216721", lng = "126.511957109074", name = "용두암")
        place1.placeCategories = mutableListOf(
            PlaceCategory(null, Category(null, "관광"), place1, true),
            PlaceCategory(null, Category(null, "명소"), place1, false)
        )
        val savedPlace1 = placeRepository.save(place1)

        val place2 = Place(lat = "33.3766655632143", lng = "126.54222094512", name = "한라산 국립공원")
        place2.placeCategories = mutableListOf(
            PlaceCategory(null, Category(null, "관광"), place2, true),
            PlaceCategory(null, Category(null, "명소"), place2, false)
        )
        val savedPlace2 = placeRepository.save(place2)

        val place3 = Place(lat = "33.5430615661113", lng = "126.669238934013", name = "함덕해수욕장")
        place3.placeCategories = mutableListOf(
            PlaceCategory(null, Category(null, "관광"), place3, true),
            PlaceCategory(null, Category(null, "명소"), place3, false)
        )
        val savedPlace3 = placeRepository.save(place3)

        val place4 = Place(lat = "33.51151689656457", lng = "126.52000128027187", name = "우진해장국")
        place4.placeCategories = mutableListOf(
            PlaceCategory(null, Category(null, "한식"), place4, true)
        )
        val savedPlace4 = placeRepository.save(place4)

        // Path
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2),
            PathPlace(path = path, place = savedPlace3, sequence = 3),
            PathPlace(path = path, place = savedPlace4, sequence = 4)
        )
        pathRepository.save(path)

        // Trip
        val trip = Trip(
            userId = user.id!!,
            title = "first trip",
            startDate = LocalDate.of(2021,11,11),
            endDate = LocalDate.of(2021,11,13)
        )
        trip.tripPaths = mutableListOf(TripPath(trip = trip, path = path, sequence = 1))
        tripRepository.save(trip)
    }
}
