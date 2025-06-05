package com.guestsby.path.domain

import com.guestsby.place.Place
import com.guestsby.place.PlaceCreator
import com.guestsby.place.PlaceRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@JvmField val prefixList = listOf("행복한", "즐거운", "신나는", "기대되는", "짜릿한")
@JvmField val nameList = listOf("여행길", "바람길", "나빗길", "유람길", "사랑길")

@Component
@Transactional
class PathCreator(
    private val pathRepository: PathRepository,
    private val pathFinder: PathFinder,
    private val pathPlaceFinder: PathPlaceFinder,
    private val placeCreator: PlaceCreator
) {
    fun create(placeRequests: List<PlaceRequest>): Path {
        val places = mutableListOf<Place>()
        val result = placeRequests.map { request ->
            val place = placeCreator.create(request)
            places.add(place)
            pathPlaceFinder.findByPlace(place).map { it.path.id }.toSet()
        }.reduce { result, set -> result.intersect(set) }

        if (result.isEmpty()) {
            return Path().apply {
                pathPlaces = places.mapIndexed { index, place ->
                    PathPlace(path = this, place = place, sequence = index + 1)
                }.toMutableList()
                this.name = makePathName() // 이름 생성
                pathRepository.save(this)
            }
        }
        return pathFinder.findById(result.first()!!)
    }

    private fun makePathName(): String {
        return prefixList[Random().nextInt(prefixList.size)] + " " +
                nameList[Random().nextInt(nameList.size)]
    }
}
