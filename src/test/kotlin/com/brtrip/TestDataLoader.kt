package com.brtrip

import com.brtrip.place.Place
import com.brtrip.restdocs.LoginUserControllerTest
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.StopRepository
import com.brtrip.trip.domain.Trip
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
    private val tripRepository: TripRepository,
    private val stopRepository: StopRepository,
    private val userRepository: UserRepository
) {
    fun sample_trip_first(userId: Long) = tripRepository.save(
        Trip(
            userId = userId,
            title = "first trip",
            startDate = LocalDate.of(2021, 5, 5),
            endDate = LocalDate.of(2021, 5, 8)
        )
    )

    fun sample_stops_first(trip: Trip) =
        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = "123",
                        lng = "456"
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = "789",
                        lng = "101"
                    ),
                    sequence = 2
                )
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
