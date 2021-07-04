package com.brtrip.common

import com.brtrip.place.Place
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.StopRepository
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripRepository
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class DataLoader(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val tripRepository: TripRepository,
    private val stopRepository: StopRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {

        // Save Sample User
        val user = userRepository.save(
            User(
                nickName = "admin",
                email = "test@.com",
                password = passwordEncoder.encode("test"),
                role = RoleType.ROLE_ADMIN,
                authProvider = AuthProvider.LOCAL
            )
        )

        val user2 = userRepository.save(
            User(
                nickName = "nick",
                email = "nick@com",
                password = passwordEncoder.encode("nick"),
                role = RoleType.ROLE_USER,
                authProvider = AuthProvider.LOCAL
            )
        )

        val trip = tripRepository.save(
            Trip(
                userId = user2.id!!,
                title = "first trip",
                startDate = LocalDate.of(2021,5,5),
                endDate = LocalDate.of(2021,5,8)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip,
                    place = Place(
                        name = "grand canyon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 2
                )
            )
        )

        val user3 = userRepository.save(
            User(
                nickName = "jason",
                email = "json@com",
                password = passwordEncoder.encode("json"),
                role = RoleType.ROLE_USER,
                authProvider = AuthProvider.LOCAL
            )
        )

        val trip2 = tripRepository.save(
            Trip(
                userId = user3.id!!,
                title = "json trip",
                startDate = LocalDate.of(2020,5,5),
                endDate = LocalDate.of(2020,5,8)
            )
        )

        stopRepository.saveAll(
            listOf(
                Stop(
                    trip = trip2,
                    place = Place(
                        name = "river park",
                        lat = BigDecimal(456.456456),
                        lng = BigDecimal(123.12321)
                    ),
                    sequence = 1
                ),
                Stop(
                    trip = trip2,
                    place = Place(
                        name = "central park",
                        lat = BigDecimal(123),
                        lng = BigDecimal(456)
                    ),
                    sequence = 2
                ),
                Stop(
                    trip = trip2,
                    place = Place(
                        name = "sapoon sapoon",
                        lat = BigDecimal(789),
                        lng = BigDecimal(101)
                    ),
                    sequence = 3
                )
            )
        )
    }
}