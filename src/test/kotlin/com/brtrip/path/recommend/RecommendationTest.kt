package com.brtrip.path.recommend

import com.brtrip.path.domain.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Category
import com.brtrip.place.Place
import com.brtrip.place.PlaceCategory
import com.brtrip.place.PlaceRepository
import com.brtrip.recommend.Recommendation
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class RecommendationTest {

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Autowired
    private lateinit var sut: Recommendation

    @Test
    fun `경로 추천`() {
        // given
        // 1번 Place
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

        // 2번 Place
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

        // 3번 Place
        val place3 = Place(
            lat = "131.131", lng = "454.454", name = "카페"
        )
        place3.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "일반시설"
                ),
                place3,
                false
            )
        )
        val savedPlace3 = placeRepository.save(place3)

        // 4번 Place
        val place4 = Place(
            lat = "787.787", lng = "323.323", name = "해변"
        )
        place4.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "힐링"
                ),
                place4,
                false
            )
        )
        val savedPlace4 = placeRepository.save(place4)

        // 5번 Place
        val place5 = Place(
            lat = "999.999", lng = "888.888", name = "절벽"
        )
        place5.placeCategories = mutableListOf(
            PlaceCategory(
                null,
                Category(
                    null, "휴식"
                ),
                place5,
                false
            )
        )
        val savedPlace5 = placeRepository.save(place5)

        // 경로 저장
        val path = Path(likeCount = 0)
        path.pathPlaces = mutableListOf(
            PathPlace(path = path, place = savedPlace1, sequence = 1),
            PathPlace(path = path, place = savedPlace2, sequence = 2)
        )
        path.name = "경로1"
        pathRepository.save(path)

        val path2 = Path(likeCount = 0)
        path2.pathPlaces = mutableListOf(
            PathPlace(path = path2, place = savedPlace2, sequence = 1),
            PathPlace(path = path2, place = savedPlace3, sequence = 2),
            PathPlace(path = path2, place = savedPlace4, sequence = 3)
        )
        path2.name = "경로2"
        pathRepository.save(path2)

        val path3 = Path(likeCount = 0)
        path3.pathPlaces = mutableListOf(
            PathPlace(path = path3, place = savedPlace1, sequence = 1),
            PathPlace(path = path3, place = savedPlace2, sequence = 2),
            PathPlace(path = path3, place = savedPlace4, sequence = 3)
        )
        path3.name = "경로3"
        pathRepository.save(path3)

        val path4 = Path(likeCount = 0)
        path4.pathPlaces = mutableListOf(
            PathPlace(path = path4, place = savedPlace3, sequence = 1),
            PathPlace(path = path4, place = savedPlace2, sequence = 2),
            PathPlace(path = path4, place = savedPlace1, sequence = 3)
        )
        path4.name = "경로4"
        pathRepository.save(path4)

        val path5 = Path(likeCount = 0)
        path5.pathPlaces = mutableListOf(
            PathPlace(path = path5, place = savedPlace5, sequence = 1),
            PathPlace(path = path5, place = savedPlace2, sequence = 2),
            PathPlace(path = path5, place = savedPlace3, sequence = 3),
            PathPlace(path = path5, place = savedPlace4, sequence = 4),
        )
        path5.name = "경로5"
        pathRepository.save(path5)

        val path6 = Path(likeCount = 0)
        path6.pathPlaces = mutableListOf(
            PathPlace(path = path6, place = savedPlace2, sequence = 1),
            PathPlace(path = path6, place = savedPlace4, sequence = 2),
            PathPlace(path = path6, place = savedPlace1, sequence = 3),
            PathPlace(path = path6, place = savedPlace5, sequence = 4)
        )
        path6.name = "경로6"
        pathRepository.save(path6)

        // when
        val pathResponse = sut.run(savedPlace2)

        // then
        println(pathResponse[0].id)
        println(pathResponse[0].likeCount)
        println(pathResponse[0].name)
        println(pathResponse[0].places)
    }
}