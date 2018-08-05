package com.example.alexmelnikov.coinspace.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.example.alexmelnikov.coinspace.BaseApp
import com.example.alexmelnikov.coinspace.model.interactors.CurrencyConverter
import com.example.alexmelnikov.coinspace.model.interactors.IUserBalanceInteractor
import com.example.alexmelnikov.coinspace.model.interactors.UserBalanceInteractor
import com.example.alexmelnikov.coinspace.model.persistance.Database
import com.example.alexmelnikov.coinspace.model.repositories.AccountsRepository
import com.example.alexmelnikov.coinspace.model.repositories.DefaultAccountsRepository
import com.example.alexmelnikov.coinspace.model.repositories.DeferOperations
import com.example.alexmelnikov.coinspace.model.repositories.DeferOperationsRepository
import com.example.alexmelnikov.coinspace.util.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    private val DATABASE_NAME = "room.db"

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return baseApp
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDeferRepository(database: Database): DeferOperations =
        DeferOperationsRepository(database.deferOperationsDao())

    @Provides
    @Singleton
    fun provideAccountsRepository(database: Database): AccountsRepository =
        DefaultAccountsRepository(database.accountDao())

    @Provides
    @Singleton
    fun providePreferencesHelper(context: Context): PreferencesHelper =
        PreferencesHelper(context)


    @Provides
    @Singleton
    fun provideCurrencyConverter(): CurrencyConverter = CurrencyConverter()


    @Provides
    @Singleton
    fun provideUserBalanceInteractor(preferencesHelper: PreferencesHelper): IUserBalanceInteractor =
        UserBalanceInteractor(preferencesHelper)
}