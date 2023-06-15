package com.arashjahani.sign_up_form.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.arashjahani.sign_up_form.SignupApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.arashjahani.sign_up_form.dao.DataRepositoryImpl
import com.arashjahani.sign_up_form.dao.DataRepository
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun providesApplication(signupApplication: SignupApplication): SignupApplication {
        return signupApplication
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository =dataRepositoryImpl


}