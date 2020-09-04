package com.allocaterite.allocaterite.core.di

import android.app.Application
import com.allocaterite.allocaterite.core.base.CommonViewModel
import com.allocaterite.allocaterite.core.base.preference.Preferences
import com.allocaterite.allocaterite.core.base.preference.PreferencesImpl
import com.allocaterite.allocaterite.feature.authorization.login.LoginViewModel
import com.allocaterite.allocaterite.feature.authorization.signUp.SignUpViewModel
import com.allocaterite.allocaterite.feature.introduction.IntroductionViewModel
import com.allocaterite.allocaterite.feature.main.dashboard.DashboardViewModel
import com.allocaterite.allocaterite.feature.main.dashboard.predictor.PredictorViewModel
import com.allocaterite.allocaterite.feature.main.portfolio.PortfolioViewModel
import com.allocaterite.allocaterite.feature.main.portfolio.portfolioDetails.PortfolioDetailsViewModel
import com.allocaterite.allocaterite.feature.main.riskMonkey.RiskMonkeyViewModel
import com.allocaterite.allocaterite.feature.main.settings.SettingsViewModel
import com.allocaterite.allocaterite.feature.main.settings.autoTrades.AutoTradesViewModel
import com.allocaterite.allocaterite.feature.main.settings.autoTrades.createContract.CreateContractViewModel
import com.allocaterite.allocaterite.feature.main.settings.autoTrades.reviewContract.ReviewContractViewModel
import com.allocaterite.allocaterite.feature.main.settings.autoTrades.signing.ContractSigningViewModel
import com.allocaterite.allocaterite.feature.main.settings.support.feedback.FeedbackViewModel
import com.allocaterite.allocaterite.feature.main.settings.notification.NotificationViewModel
import com.allocaterite.allocaterite.feature.main.settings.promocode.PromocodeViewModel
import com.allocaterite.allocaterite.feature.main.settings.subscription.SubscriptionViewModel
import com.allocaterite.allocaterite.feature.main.stategyDetails.StrategyDetailsViewModel
import com.allocaterite.allocaterite.model.NetManager
import com.allocaterite.allocaterite.model.NetManagerImpl
import com.allocaterite.allocaterite.model.auth.AuthCognitoManager
import com.allocaterite.allocaterite.model.auth.AuthCognitoManagerImpl
import com.allocaterite.allocaterite.model.client.AllocateClient
import com.allocaterite.allocaterite.repository.auth.AuthRepository
import com.allocaterite.allocaterite.repository.auth.AuthRepositoryImpl
import com.allocaterite.allocaterite.repository.autoTrades.AutoTradesRepository
import com.allocaterite.allocaterite.repository.autoTrades.AutoTradesRepositoryImpl
import com.allocaterite.allocaterite.repository.dashboard.DashboardRepository
import com.allocaterite.allocaterite.repository.dashboard.DashboardRepositoryImpl
import com.allocaterite.allocaterite.repository.introducation.IntroductionRepository
import com.allocaterite.allocaterite.repository.introducation.IntroductionRepositoryImpl
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
        viewModel { SettingsViewModel(get()) }
        viewModel { PortfolioViewModel(get(), get(), get()) }
        viewModel { PredictorViewModel(get()) }
        viewModel { PortfolioDetailsViewModel(get(), get(), get()) }
        viewModel { AutoTradesViewModel(get(), get()) }
        viewModel { CreateContractViewModel() }
        viewModel { ContractSigningViewModel(get(), get()) }
        viewModel { ReviewContractViewModel(get(), get(), get()) }
        viewModel { FeedbackViewModel(get(), get()) }
        viewModel { NotificationViewModel(get(), get()) }
        viewModel { SubscriptionViewModel(get(), get()) }
        viewModel { PromocodeViewModel(get(), get()) }
    }

    private val repositoryModules = module {
        factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<DashboardRepository> { DashboardRepositoryImpl(get(), get()) }
        factory<StrategyRepository> { StrategyRepositoryImpl(get(), get()) }
        factory<RiskMonkeyRepository> { RiskMonkeyRepositoryImpl(get(), get()) }
        factory<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
        factory<IntroductionRepository> { IntroductionRepositoryImpl(get()) }
        factory<PortfolioRepository> { PortfolioRepositoryImpl(get(), get()) }
        factory<AutoTradesRepository> { AutoTradesRepositoryImpl(get(), get()) }
    }

    private val managersModules = module {
        single<Preferences> { PreferencesImpl(androidContext()) }
        single<AuthCognitoManager> { AuthCognitoManagerImpl() }
        single<NetManager> { NetManagerImpl(AllocateClient().getApi()) }
    }
}