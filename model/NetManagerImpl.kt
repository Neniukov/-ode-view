package com.allocaterite.allocaterite.model

import com.allocaterite.allocaterite.core.entity.*
import com.allocaterite.allocaterite.model.api.AllocateApi
import com.allocaterite.allocaterite.model.entity.*

class NetManagerImpl(private val api: AllocateApi) : NetManager {

    override suspend fun getStrategies(token: String): List<TrackerStrategy> {
        return api.getTrackersStrategies(token).await()
    }

    override suspend fun getAllStrategies(token: String): List<Strategy> {
        return api.getStrategies(token).await()
    }

    override suspend fun getMarketData(token: String): List<Indicator> {
        return api.getMarketData(token).await()
    }

    override suspend fun getUser(token: String): User {
        return api.getUser(token).await()
    }

    override suspend fun getRisk(
        token: String,
        request: RequestRisk,
        portfolio: List<PortfolioCalculator>
    ): MonkeyRisk {
        return api.getRisk(
            token,
            PortfolioRiskRequest(
                portfolio,
                "",
                request
            )
        ).await()
    }

    override suspend fun getRiskAll(token: String): RiskAll {
        return api.getRiskAll(token).await()
    }

}