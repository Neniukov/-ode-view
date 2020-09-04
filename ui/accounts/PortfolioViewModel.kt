package com.allocaterite.allocaterite.feature.main.portfolio

import androidx.lifecycle.MutableLiveData
import com.allocaterite.allocaterite.core.base.CommonViewModel
import com.allocaterite.allocaterite.core.entity.*
import com.allocaterite.allocaterite.repository.auth.AuthRepository
import com.allocaterite.allocaterite.repository.dashboard.DashboardRepository
import com.allocaterite.allocaterite.repository.portfolio.PortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

class PortfolioViewModel(
    authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
    private val portfolioRepository: PortfolioRepository
) : CommonViewModel(authRepository, dashboardRepository) {

    val trackersPortfolioLD = MutableLiveData<List<PlaidProfile>>()
    val removeAccountsLD = MutableLiveData<Boolean>()
    val allAccountsLD = MutableLiveData<List<AccountForConnect>>()

    private val accountsForAdding = HashSet<AccountForConnect>()
    private val selectedAccounts = arrayListOf<PlaidProfile>()

    fun getAllPortfolio() {
        launch(coroutineExceptionHandler { getAllPortfolio() }) {
            val trackersPortfolio =
                withContext(Dispatchers.IO) { dashboardRepository.getPlaidItemsForPortfolio(true) }
            val trackers = withContext(Dispatchers.IO) { dashboardRepository.getUserTrackers() }
            trackersPortfolio.forEach { plaidPortfolio ->
                trackers.forEach { tracker ->
                    if (plaidPortfolio.accountId == tracker.accountId) {
                        plaidPortfolio.status = tracker.status
                    }
                }
            }
            trackersPortfolio.forEach { plaidPortfolio ->
                val monkeyRisk = dashboardRepository.getRisk(
                    RequestRisk(
                        RiskType.PORTFOLIO.name.toLowerCase(),
                        plaidPortfolio.accountId
                    )
                )
                if (monkeyRisk?.data?.cVar != null) {
                    var risk = 0.0
                    var isMinus = false
                    monkeyRisk.data.maxDrawDownVal?.let {
                        risk = sqrt(monkeyRisk.data.cVar * monkeyRisk.data.maxDrawDownVal)
                        if (monkeyRisk.data.cVar < 0.0 && monkeyRisk.data.maxDrawDownVal < 0.0) isMinus =
                            true
                    }
                    plaidPortfolio.risk = if (isMinus) -risk else risk
                }
            }
            portfolioRepository.savePortfolioDetails(trackersPortfolio)
            trackersPortfolioLD.value = trackersPortfolio
        }
    }

    fun selectAccount(item: PlaidProfile): Boolean {
        var isNewItem = true
        if (selectedAccounts.isNotEmpty()) {
            val items = selectedAccounts
            val iterator = items.iterator()
            while (iterator.hasNext()) {
                val plaid = iterator.next()
                if (plaid.name == item.name) {
                    isNewItem = false
                    iterator.remove()
                }
            }
            if (isNewItem) selectedAccounts.add(item)
        } else {
            selectedAccounts.add(item)
        }
        return selectedAccounts.isNotEmpty()
    }

    fun removeAccounts() {
        val accounts = arrayListOf<InfoBankAcc>()
        selectedAccounts.forEach { accounts.add(InfoBankAcc(it.accountId, it.itemId)) }
        launch(coroutineExceptionHandler { removeAccounts() }) {
            removeAccountsLD.value =
                withContext(Dispatchers.IO) {
                    portfolioRepository.removeAccounts(AccountsRemoved(accounts))
                }
        }
    }

    fun getAllAccounts(itemId: String) {
        launch(coroutineExceptionHandler { getAllAccounts(itemId) }) {
            allAccountsLD.value =
                withContext(Dispatchers.IO) { portfolioRepository.getAllAccounts(itemId) }
        }
    }

    fun selectAddAccount(item: AccountForConnect): Boolean {
        if (accountsForAdding.isNotEmpty()) {
            val items = accountsForAdding
            items.forEach { if (it == item) accountsForAdding.remove(it) }
        } else {
            accountsForAdding.add(item)
        }
        return accountsForAdding.isNotEmpty()
    }

    fun addAccounts() {
        val accounts = arrayListOf<AccountForConnect>()
        accountsForAdding.forEach { accounts.add(it) }
        launch(coroutineExceptionHandler { addAccounts() }) {
            informationAccLD.value =
                withContext(Dispatchers.IO) { portfolioRepository.addAccounts(accounts) }
        }
    }
}