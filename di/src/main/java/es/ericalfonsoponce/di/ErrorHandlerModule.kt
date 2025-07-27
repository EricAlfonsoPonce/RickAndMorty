package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.data.handler.local.ErrorLocalHandlerImpl
import es.ericalfonsoponce.data.handler.remote.ErrorRemoteHandlerImpl
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerModule {
    @Provides
    @Singleton
    @Named("remote")
    fun providesErrorRemoteHandler(): ErrorHandler = ErrorRemoteHandlerImpl()

    @Provides
    @Singleton
    @Named("local")
    fun providesErrorLocalHandler(): ErrorHandler = ErrorLocalHandlerImpl()
}