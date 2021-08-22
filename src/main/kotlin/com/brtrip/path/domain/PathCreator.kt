package com.brtrip.path.domain

import com.brtrip.place.Place
import com.brtrip.place.PlaceCreator
import com.brtrip.place.PlaceRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

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
                pathRepository.save(this)
            }
        }
        return pathFinder.findById(result.first()!!)
    }
}
