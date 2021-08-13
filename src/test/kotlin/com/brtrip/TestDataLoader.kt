package com.brtrip

import com.brtrip.path.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathPlaceRepository
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripPath
import com.brtrip.trip.domain.TripPathRepository
import com.brtrip.trip.domain.TripRepository
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@Component
@ActiveProfiles("test")
internal class TestDataLoader(
    private val userRepository: UserRepository,
    private val tripRepository: TripRepository,
    private val pathRepository: PathRepository,
    private val placeRepository: PlaceRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun sample_trip_first(userId: Long) = tripRepository.save(
        Trip(
            userId = userId,
            title = "first trip",
            startDate = LocalDate.of(2021, 8, 5),
            endDate = LocalDate.of(2021, 8, 8),
        )
    )

    fun sample_path_first(pathId: Long) = pathRepository.save(
        Path(
            id = pathId,
            likeCount = 1L
        )
    )

    fun sample_place_first(place: Place) = placeRepository.save(place)

    fun sample_trip_path_first(trip: Trip, path: Path) = tripPathRepository.save(
        TripPath(
            trip = trip,
            path = path,
            sequence = 1
        )
    )

    fun sample_path_place_first(path: Path, place: Place, sequence: Int) = pathPlaceRepository.save(
        PathPlace(
            path = path,
            place = place,
            sequence = sequence
        )
    )

    fun sample_user() = userRepository.save(
        User(
            id = 1L,
            nickName = "여행가",
            email = "trip@com",
            password = "test123",
            role = RoleType.ROLE_USER,
            authProvider = AuthProvider.KAKAO
        )
    )
}
