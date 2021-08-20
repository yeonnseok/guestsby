package com.brtrip.favorite

import com.brtrip.TestDataLoader
import com.brtrip.favorite.domain.FavoriteFinder
import com.brtrip.path.domain.PathPlaceFinder
import com.brtrip.place.Place
import com.brtrip.trip.domain.TripFinder
import com.brtrip.trip.domain.TripPathFinder
import com.brtrip.trip.domain.TripUpdater
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class FavoriteFinderTest {

    @Autowired
    private lateinit var sut: FavoriteFinder

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `찜한 여행 목록 불러오기`() {
        // given
        val place1 = Place(
            lat = "123",
            lng = "456",
            name = "central park"
        )
        val place2 = Place(
            lat = "789",
            lng = "101",
            name = "grand canyon"
        )

        // user 저장
        val user = testDataLoader.sample_user()

        // trip 저장
        val trip = testDataLoader.sample_trip_first(1L)

        // path 저장
        var path = testDataLoader.sample_path_first(1L)

        // place 저장
        testDataLoader.sample_place_first(place1)
        testDataLoader.sample_place_first(place2)

        // tripPath 저장
        testDataLoader.sample_trip_path_first(trip, path)

        // pathPlace 저장
        val pathPlace1 = testDataLoader.sample_path_place_first(path, place1, 1)
        val pathPlace2 = testDataLoader.sample_path_place_first(path, place2, 2)

        path.pathPlaces.add(pathPlace1)
        path.pathPlaces.add(pathPlace2)

        // favorite 저장
        testDataLoader.sample_favorite(user, path)

        // when
        val result = sut.findByUserId(user.id!!)

        // then
        result[0].user.id shouldBe user.id
        result[0].user.nickName shouldBe user.nickName
        result[0].user.email shouldBe user.email
        result[0].user.role shouldBe user.role
        result[0].user.authProvider shouldBe user.authProvider

        result[0].path.id shouldBe path.id

        result[0].path.pathPlaces[0].id shouldBe pathPlace1.id
        result[0].path.pathPlaces[0].place shouldBe pathPlace1.place

        result[0].path.pathPlaces[1].id shouldBe pathPlace2.id
        result[0].path.pathPlaces[1].place shouldBe pathPlace2.place
    }
}