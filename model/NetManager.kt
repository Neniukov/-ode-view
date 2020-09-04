package com.allocaterite.allocaterite.model

import com.allocaterite.allocaterite.core.entity.*
import com.allocaterite.allocaterite.model.entity.*

interface NetManager {

    suspend fun getStrategies(token: String): List<TrackerStrategy>

    suspend fun getAllStrategies(token: String): List<Strategy>

    suspend fun getMarketData(token: String): List<Indicator>

    suspend fun getUser(token: String): User

    suspend fun getRisk(
        token: String,
        request: RequestRisk,
        portfolio: List<PortfolioCalculator> = emptyList()
    ): MonkeyRisk

    suspend fun getRiskAll(token: String): RiskAll

}