package kr.co.guestsby.api.domain.ranking

interface RankingService {
    val rankingType: Ranking.RankingType

    suspend fun getRankingBoard(limit: Int?): List<Ranking>

    suspend fun getRanking(id: String): Ranking
}
