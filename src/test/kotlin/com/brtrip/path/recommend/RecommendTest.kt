package com.brtrip.path.recommend

import com.brtrip.path.domain.Path
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class RecommendTest {
    @Autowired
    private lateinit var recommendation: Recommendation

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var pathRepository: PathRepository

    @Test
    fun `경로 추천`() {
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

        // when
        val pathResponse = recommendation.run(place1)

        // then
        println(pathResponse[0].id)
        println(pathResponse[0].likeCount)
        println(pathResponse[0].name)
        println(pathResponse[0].places)
    }
}