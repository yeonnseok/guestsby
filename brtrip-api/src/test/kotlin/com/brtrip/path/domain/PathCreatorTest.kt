package com.brtrip.path.domain

import com.brtrip.place.*
import com.brtrip.place.controller.request.PlaceRequest
import io.kotlintest.matchers.collections.shouldNotBeOneOf
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
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
            PlaceRequest(lat = "123.123", lng = "456.456", "용두암", keywords = arrayOf("힐링")),
            PlaceRequest(lat = "789.789", lng = "321.321", "한라산 국립 공원", keywords = arrayOf("관광")),
            PlaceRequest(lat = "000.000", lng = "000.000", "공항", keywords = arrayOf("주요시설"))
        )

        // when
        val findPath = sut.create(places)

        // then
        findPath.id shouldBe path.id
    }

    @Test
    fun `경로가 DB에 존재하지 않을 때`() {
        // given
        val place1 = Place(
            lat = "123.123", lng = "456.456", name = "용두암"
        )
        place1.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place1,
                false
            )
        )
        val savedPlace1 = placeRepository.save(place1)

        val place2 = Place(
            lat = "789.789", lng = "321.321", name = "한라산 국립 공원"
        )
        place2.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "관광"
                ),
                place2,
                false
            )
        )
        val savedPlace2 = placeRepository.save(place2)

        val place3 = Place(
            lat = "000.000", lng = "000.000", name = "공항"
        )
        place3.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "주요시설"
                ),
                place3,
                false
            )
        )
        val savedPlace3 = placeRepository.save(place3)

        val path = Path()
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2),
            PathPlace(path = path, place = savedPlace3, sequence = 3)
        )
        pathRepository.save(path)

        val otherPath = Path()
        otherPath.pathPlaces = mutableListOf(
            PathPlace(path = otherPath, place = savedPlace1, sequence = 1),
            PathPlace(path = otherPath, place = savedPlace3, sequence = 2)
        )
        pathRepository.save(otherPath)

        val places: List<PlaceRequest> = listOf(
            PlaceRequest(lat = "123.123", lng = "456.456", "용두암", keywords = arrayOf("힐링")),
            PlaceRequest(lat = "789.789", lng = "321.321", "한라산 국립 공원", keywords = arrayOf("관광")),
            PlaceRequest(lat = "135.135", lng = "135.135", "카페", keywords = arrayOf("일반시설"))
        )

        // when
        val findPath = sut.create(places)

        // then
        findPath.id shouldNotBeOneOf listOf(path.id, otherPath.id)
    }
}
