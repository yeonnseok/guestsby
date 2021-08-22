package com.brtrip.place

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceService(
    private val placeRepository: PlaceRepository
) {

}
