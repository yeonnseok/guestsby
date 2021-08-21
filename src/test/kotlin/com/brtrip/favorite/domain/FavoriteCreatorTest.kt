package com.brtrip.favorite.domain

import com.brtrip.TestDataLoader
import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.favorite.domain.FavoriteCreator
import com.brtrip.place.Place
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
class FavoriteCreatorTest {
    @Autowired
    private lateinit var sut: FavoriteCreator

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Test
    fun `찜 저장하기`() {
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

        result.path.pathPlaces[0].id shouldBe pathPlace1.id
        result.path.pathPlaces[0].place shouldBe pathPlace1.place

        result.path.pathPlaces[1].id shouldBe pathPlace2.id
        result.path.pathPlaces[1].place shouldBe pathPlace2.place
    }
}