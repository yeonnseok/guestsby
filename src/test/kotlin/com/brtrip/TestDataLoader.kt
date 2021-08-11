package com.brtrip

import com.brtrip.place.Place
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripRepository
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

//@Component
@ActiveProfiles("test")
internal class TestDataLoader(
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository
) {
    fun sample_trip_first(userId: Long) = tripRepository.save(
        Trip(
            userId = userId,
            title = "first trip",
            startDate = LocalDate.of(2021, 5, 5),
            endDate = LocalDate.of(2021, 5, 8),
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
