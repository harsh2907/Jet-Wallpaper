package com.example.shared.di

import com.example.shared.data.network.WallpaperClient
import com.example.shared.data.network.createHttpClient
import com.example.shared.data.repository.OnlineWallpaperRepositoryImpl
import com.example.shared.domain.repository.OnlineWallpaperRepository
import com.example.shared.presentation.home.SharedViewModel
import io.ktor.client.engine.darwin.Darwin
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        createHttpClient(
            engine = Darwin.create()
        )
    }

    singleOf(::WallpaperClient)


    singleOf(::OnlineWallpaperRepositoryImpl).bind<OnlineWallpaperRepository>()

    viewModelOf(::SharedViewModel)

}