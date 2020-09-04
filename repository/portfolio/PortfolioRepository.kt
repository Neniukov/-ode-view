package com.allocaterite.allocaterite.repository.portfolio

import com.allocaterite.allocaterite.core.entity.*

interface PortfolioRepository {

    suspend fun removeAccounts(accounts: AccountsRemoved): Boolean

    suspend fun addAccounts(accounts: List<AccountForConnect>): AccountInformation?

    suspend fun getAllAccounts(itemId: String): List<AccountForConnect>

    suspend fun savePortfolioDetails(portfolios: List<PlaidProfile>)

    suspend fun getPortfolioDetail(accountId: String): PlaidProfile?

    suspend fun getRebalanceInfo(): RebalanceInfo?

    suspend fun getTrades(trackerId: String): List<Trades>

    suspend fun setMargin(isEnable: Boolean): Boolean
}