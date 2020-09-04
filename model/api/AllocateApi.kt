package com.allocaterite.allocaterite.model.api

import com.allocaterite.allocaterite.core.entity.*
import com.allocaterite.allocaterite.model.entity.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AllocateApi {

    @GET("strategies")
    fun getStrategies(@Header("Authorization") token: String): Deferred<List<Strategy>>

    @GET("risk-monkey/momentum")
    fun getMarketData(@Header("Authorization") token: String): Deferred<List<Indicator>>

    @GET("users")
    fun getUser(@Header("Authorization") token: String): Deferred<User>

    @POST("risk-monkey")
    fun getRisk(
        @Header("Authorization") token: String,
        @Body body: PortfolioRiskRequest
    ): Deferred<MonkeyRisk>

    @GET("risk-monkey/all")
    fun getRiskAll(@Header("Authorization") token: String): Deferred<RiskAll>

    @POST("plaid/link")
    fun sendPlaidAccount(
        @Header("Authorization") token: String,
        @Body body: PlaidAccounts
    ): Deferred<AccountInformation>
}