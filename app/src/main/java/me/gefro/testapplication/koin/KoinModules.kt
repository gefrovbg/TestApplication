package me.gefro.testapplication.koin

import android.content.Context
import me.gefro.data.ktor.KtorApi
import me.gefro.data.ktor.RepositoriesApiService
import me.gefro.data.repository.DownloadedFilesRepositoryImpl
import me.gefro.data.repository.RepositoriesRepositoryImpl
import me.gefro.domain.bl.repository.DownloadedFilesRepository
import me.gefro.domain.bl.repository.RepositoriesRepository
import me.gefro.domain.bl.usecases.DownloadRepositoryArchiveUseCase
import me.gefro.domain.bl.usecases.GetAllDownloadedRepositoriesFromDBUseCase
import me.gefro.domain.bl.usecases.GetListRepositoriesBySearchUseCase
import me.gefro.testapplication.paging.SearchRepositoriesPagingData
import me.gefro.testapplication.services.DownloadService
import me.gefro.testapplication.ui.screens.downloaded.DownloadedFilesScreenViewModel
import me.gefro.testapplication.ui.screens.main.MainScreenViewModel
import me.gefro.testapplication.ui.screens.splash.SplashScreenViewModel
import me.gefro.testapplication.ui.tools.SafeArea
import org.koin.dsl.module

/** <----- App -----> */

private val viewModelModule = module {
    single { SplashScreenViewModel() }
    single { MainScreenViewModel(get(), get(), get()) }
    single { DownloadedFilesScreenViewModel(get()) }
}

private val toolsModule = module {
    single { SafeArea() }
}
private val pagingModule = module {
    single { SearchRepositoriesPagingData(get()) }
}

private val servicesModule = module {
    single { DownloadService() }
}

private val appModules = listOf(
    viewModelModule,
    toolsModule,
    pagingModule,
    servicesModule
)


/** <----- Data -----> */

private val dataApiModule = module {
    single { KtorApi() }
    single { RepositoriesApiService(get(), get()) }
}

private val dataModules = listOf(
    dataApiModule
)

/** <----- Domain -----> */

private fun domainRepositoryModule(context: Context) = module {
    single <RepositoriesRepository>{ RepositoriesRepositoryImpl(get()) }
    single <DownloadedFilesRepository>{ DownloadedFilesRepositoryImpl(context = context) }
}

private val domainUseCasesModule = module {
    factory { GetListRepositoriesBySearchUseCase(get()) }
    factory { DownloadRepositoryArchiveUseCase(get()) }
    factory { GetAllDownloadedRepositoriesFromDBUseCase(get()) }
}

private fun domainModules(context: Context) = listOf(
    domainRepositoryModule(context = context),
    domainUseCasesModule
)

/** <----- Main -----> */

fun mainModules(context: Context) = appModules + dataModules + domainModules(context = context)