package com.brtrip.like.domain

import com.brtrip.common.exceptions.AlreadyExistedException
import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.like.controller.request.LikeRequest
import com.brtrip.trip.domain.TripFinder
import com.brtrip.user.domain.UserFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LikeService(
    private val userFinder: UserFinder,
    private val tripFinder: TripFinder,
    private val likeRepository: LikeRepository
) {
    // likeCount 동시성 문제
    fun like(userId: Long, request: LikeRequest): Like {
        val user = userFinder.findById(userId)
        val trip = tripFinder.findById(request.tripId)

        var like = likeRepository.findByUserAndTrip(user, trip)
        if (like != null) {
            throw AlreadyExistedException("이미 생성된 좋아요 입니다.")
        }

        like = Like(trip = trip, user = user)
        trip.likeCount++
        return likeRepository.save(like)
    }

    fun delete(userId: Long, request: LikeRequest) {
        val user = userFinder.findById(userId)
        val trip = tripFinder.findById(request.tripId)

        val like = likeRepository.findByUserAndTrip(user, trip)
        if (like == null) {
            throw NotFoundException("존재하지 않는 좋아요 입니다.")
        }

        trip.likeCount--
        likeRepository.deleteById(like.id!!)
    }
}