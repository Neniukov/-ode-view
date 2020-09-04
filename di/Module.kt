package com.allocaterite.allocaterite.core.di

import android.app.Application
import com.allocaterite.allocaterite.repository.portfolio.PortfolioRepository
import com.allocaterite.allocaterite.repository.portfolio.PortfolioRepositoryImpl
import com.allocaterite.allocaterite.repository.riskMonkey.RiskMonkeyRepository
import com.allocaterite.allocaterite.repository.riskMonkey.RiskMonkeyRepositoryImpl
import com.allocaterite.allocaterite.repository.settings.SettingsRepository
import com.allocaterite.allocaterite.repository.settings.SettingsRepositoryImpl
import com.allocaterite.allocaterite.repository.strategy.StrategyRepository
import com.allocaterite.allocaterite.repository.strategy.StrategyRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

object Module {

    fun initModules(context: Application) {
        startKoin {
            androidContext(context)
            modules(arrayListOf(viewModules, managersModules, repositoryModules))
        }
    }

    private val viewModules = module {
        viewModel { IntroductionViewModel(get()) }
        viewModel { SignUpViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { DashboardViewModel(get(), get(), androidContext()) }
        viewModel { CommonViewModel(get(), get()) }
        viewModel { StrategyDetailsViewModel(get(), get(), get()) }
        viewModel { RiskMonkeyViewModel(get(), get()) }
      }

    private val repositoryModules = module {
        factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<DashboardRepository> { DashboardRepositoryImpl(get(), get()) }
        factory<StrategyRepository> { StrategyRepositoryImpl(get(), get()) }
        factory<PortfolioRepository> { PortfolioRepositoryImpl(get(), get()) }
    }

    private val managersModules = module {
        single<Preferences> { PreferencesImpl(androidContext()) }
        single<AuthCognitoManager> { AuthCognitoManagerImpl() }
        single<NetManager> { NetManagerImpl(AllocateClient().getApi()) }
    }
}