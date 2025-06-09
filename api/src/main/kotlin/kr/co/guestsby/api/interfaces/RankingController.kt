package kr.co.guestsby.api.interfaces

import kr.co.guestsby.api.domain.ranking.Ranking
import kr.co.guestsby.api.domain.ranking.RankingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class RankingController(
    rankingServices: List<RankingService>,
) {
    private val rankingServiceMap: Map<Ranking.RankingType, RankingService> =
        rankingServices.associateBy { it.rankingType }

    @GetMapping("/rankings")
    suspend fun getRankingBoard(
        @RequestParam("ranking_type") rankingType: Ranking.RankingType,
        @RequestParam("limit", required = false) limit: Int?,
    ): ResponseEntity<List<Ranking>> {
        val rankings = determineRankingService(rankingType).getRankingBoard(limit)
        return ResponseEntity.ok(rankings)
    }

    @GetMapping("/rankings/{ranking_type}/{id}")
    suspend fun getRanking(
        @PathVariable("ranking_type") rankingType: Ranking.RankingType,
        @PathVariable id: String,
    ): ResponseEntity<Ranking> {
        val ranking = determineRankingService(rankingType).getRanking(id)
        return ResponseEntity.ok(ranking)
    }

    private fun determineRankingService(rankingType: Ranking.RankingType): RankingService =
        rankingServiceMap[rankingType]
            ?: throw IllegalArgumentException("### 지원하지 않는 랭킹 타입 입니다.")
}
