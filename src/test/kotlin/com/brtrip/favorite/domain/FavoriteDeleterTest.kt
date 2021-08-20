package com.brtrip.favorite.domain

import com.brtrip.TestDataLoader
import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.favorite.domain.FavoriteDeleter
import com.brtrip.favorite.domain.FavoriteRepository
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
internal class FavoriteDeleterTest {

    @Autowired
    private lateinit var sut: FavoriteDeleter

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    @Autowired
    private lateinit var favoriteRepository: FavoriteRepository

    @Test
    fun `찜 삭제 api`() {
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

        // path 저장
        var path = testDataLoader.sample_path_first(1L)

        // place 저장
        testDataLoader.sample_place_first(place1)
        testDataLoader.sample_place_first(place2)

        // pathPlace 저장
        val pathPlace1 = testDataLoader.sample_path_place_first(path, place1, 1)
        val pathPlace2 = testDataLoader.sample_path_place_first(path, place2, 2)

        path.pathPlaces.add(pathPlace1)
        path.pathPlaces.add(pathPlace2)

        // favorite 저장
        val favorite = testDataLoader.sample_favorite(user, path)

        // when
        sut.delete(favorite.id!!)
        val deleted = favoriteRepository.findById(favorite.id!!)
            .orElseThrow { NotFoundException("찜 목록이 없습니다.") }

        // then
        deleted.deleted shouldBe true
    }
}