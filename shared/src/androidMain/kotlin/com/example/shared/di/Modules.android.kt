package com.example.shared.di

import com.example.shared.data.network.WallpaperClient
import com.example.shared.data.network.createHttpClient
import com.example.shared.data.repository.OnlineWallpaperRepositoryImpl
import com.example.shared.domain.repository.OnlineWallpaperRepository
import com.example.shared.presentation.home.SharedViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClient> {
        createHttpClient(
            engine = OkHttp.create()
        )
    }

    singleOf(::WallpaperClient)

    singleOf(::OnlineWallpaperRepositoryImpl).bind<OnlineWallpaperRepository>()

    viewModelOf(::SharedViewModel)
}