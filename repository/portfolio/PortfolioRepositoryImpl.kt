package com.allocaterite.allocaterite.repository.portfolio

import com.allocaterite.allocaterite.core.base.preference.Preferences
import com.allocaterite.allocaterite.core.entity.*
import com.allocaterite.allocaterite.model.NetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlin.math.sqrt

class PortfolioRepositoryImpl(
    private val netManager: NetManager,
    private val prefs: Preferences
) : PortfolioRepository {

    override suspend fun getRebalanceInfo(): RebalanceInfo? {
        return prefs.getJWTToken()?.let { netManager.getRebalanceInfo(it) }
    }

    override suspend fun getAllAccounts(itemId: String) =
        prefs.getJWTToken()?.let { netManager.getAllAccounts(it, itemId) } ?: emptyList()

    override suspend fun removeAccounts(accounts: AccountsRemoved) =
        prefs.getJWTToken()?.let { netManager.removeAccounts(it, accounts) } ?: false

    override suspend fun addAccounts(accounts: List<AccountForConnect>) =
        prefs.getJWTToken()?.let { netManager.addAccounts(it, accounts) }

    override suspend fun savePortfolioDetails(portfolios: List<PlaidProfile>) {
        val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
        val plaidItemsString = gsonBuilder.toJson(portfolios)
        prefs.savePlaidPortfolios(plaidItemsString)
    }

    override suspend fun getPortfolioDetail(accountId: String): PlaidProfile? {
        if (prefs.getRiskPortfolios().isEmpty()) {
            prefs.getJWTToken()?.let { token ->
                val trackers = netManager.getPlaids(token)
                if (trackers.isNotEmpty()) {
                    trackers.forEach { tracker ->
                        tracker.accounts.forEach { plaidProfile ->
                            if (plaidProfile.accountId == accountId) {
                                val monkeyRisk = netManager.getRisk(
                                    token,
                                    RequestRisk(RiskType.PORTFOLIO.name.toLowerCase(), accountId)
                                )
                                if (monkeyRisk?.data?.cVar != null) {
                                    var risk = 0.0
                                    var isMinus = false
                                    monkeyRisk.data.maxDrawDownVal?.let {
                                        risk =
                                            sqrt(monkeyRisk.data.cVar * monkeyRisk.data.maxDrawDownVal)
                                        if (monkeyRisk.data.cVar < 0.0 && monkeyRisk.data.maxDrawDownVal < 0.0) isMinus =
                                            true
                                    }
                                    plaidProfile.risk = if (isMinus) -risk else risk
                                    return plaidProfile
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val gson = Gson()
            val plaidsType = object : TypeToken<List<PlaidProfile>>() {}.type
            val plaids = gson.fromJson<List<PlaidProfile>?>(prefs.getRiskPortfolios(), plaidsType)
            plaids?.forEach { plaidProfile ->
                if (plaidProfile.accountId == accountId) {
                    return plaidProfile
                }
            }
        }
        return null
    }

    override suspend fun getTrades(trackerId: String): List<Trades> {
        return prefs.getJWTToken()?.let { netManager.getTrades(it, trackerId) } ?: emptyList()
    }

    override suspend fun setMargin(isEnable: Boolean): Boolean {
        return prefs.getJWTToken()?.let {
            val result = netManager.setMargin(it, Margin(isEnable))
            if (result.isEmpty()) return false else true
        } ?: false
    }
}