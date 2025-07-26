package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.data.handler.ErrorRemoteHandlerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerModule {
    @Provides
    @Singleton
    fun providesErrorRemoteHandler(): ErrorHandler = ErrorRemoteHandlerImpl()
}