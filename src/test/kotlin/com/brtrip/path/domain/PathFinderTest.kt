package com.brtrip.path.domain

import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
import io.kotlintest.matchers.collections.shouldNotBeOneOf
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class PathCreatorTest {

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var sut: PathCreator

    @Test
    fun `경로가 DB에 존재할 때`() {
        // given
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2),
            PathPlace(path = path, place = place3, sequence = 3)
        )
        pathRepository.save(path)

        val otherPath = Path()
        otherPath.pathPlaces = mutableListOf(
            PathPlace(path = otherPath, place = place1, sequence = 1),
            PathPlace(path = otherPath, place = place3, sequence = 2)
        )
        pathRepository.save(otherPath)

        val places: List<PlaceRequest> = listOf(
            PlaceRequest(lat = "123.123", lng = "456.456", "용두암"),
            PlaceRequest(lat = "789.789", lng = "321.321", "한라산 국립 공원"),
            PlaceRequest(lat = "000.000", lng = "000.000", "공항")
        )

        // when
        val findPath = sut.create(places)

        // then
        findPath.id shouldBe path.id
    }

    @Test
    fun `경로가 DB에 존재하지 않을 때`() {
        // given
        val place1 = placeRepository.save(Place(lat = "123.123", lng = "456.456", name = "용두암"))
        val place2 = placeRepository.save(Place(lat = "789.789", lng = "321.321", name = "한라산 국립 공원"))
        val place3 = placeRepository.save(Place(lat = "000.000", lng = "000.000", name = "공항"))
        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = place1, sequence = 1),
            PathPlace(path = path, place = place2, sequence = 2),
            PathPlace(path = path, place = place3, sequence = 3)
        )
        pathRepository.save(path)

        val otherPath = Path()
        otherPath.pathPlaces = mutableListOf(
            PathPlace(path = otherPath, place = place1, sequence = 1),
            PathPlace(path = otherPath, place = place3, sequence = 2)
        )
        pathRepository.save(otherPath)

        val places: List<PlaceRequest> = listOf(
            PlaceRequest(lat = "123.123", lng = "456.456", "용두암"),
            PlaceRequest(lat = "789.789", lng = "321.321", "한라산 국립 공원"),
            PlaceRequest(lat = "135.135", lng = "135.135", "카페")
        )

        // when
        val findPath = sut.create(places)

        // then
        findPath.id shouldNotBeOneOf listOf(path.id, otherPath.id)
    }
}
