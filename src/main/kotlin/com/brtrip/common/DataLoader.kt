package com.brtrip.common

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

        // Save Sample User
        val user = userRepository.save(
            User(
                nickName = "admin",
                email = "test@com",
                password = passwordEncoder.encode("test"),
                role = RoleType.ROLE_ADMIN,
                authProvider = AuthProvider.LOCAL
            )
        )

        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2)
        )
        pathRepository.save(path)

        val trip = Trip(
            userId = user.id!!,
            title = "first trip",
            startDate = LocalDate.of(2021,8,11),
            endDate = LocalDate.of(2021,8,13)
        )
        trip.tripPaths = mutableListOf(TripPath(trip = trip, path = path, sequence = 1))
        tripRepository.save(trip)

        val anotherPath = Path(likeCount = 0)
        anotherPath.pathPlaces = mutableListOf(
            PathPlace(path = anotherPath, place = place1, sequence = 1),
            PathPlace(path = anotherPath, place = place3, sequence = 2),
            PathPlace(path = anotherPath, place = place2, sequence = 3)
        )
        pathRepository.save(anotherPath)
    }
}
