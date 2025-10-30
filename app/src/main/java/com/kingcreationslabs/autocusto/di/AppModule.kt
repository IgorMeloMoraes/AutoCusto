package com.kingcreationslabs.autocusto.di

import com.kingcreationslabs.autocusto.data.repository.UserPreferencesRepositoryImpl
import com.kingcreationslabs.autocusto.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.kingcreationslabs.autocusto.data.repository.AuthRepositoryImpl
import com.kingcreationslabs.autocusto.domain.repository.AuthRepository

/**
 * Módulo Hilt que provê dependências em escopo de
 * Singleton (nível de Application).
 *
 * Nota: Convertido para 'abstract class' para suportar o @Binds.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Provê a implementação do [UserPreferencesRepository].
     *
     * @Binds é usado para dizer ao Hilt qual implementação usar
     * quando uma interface for solicitada.
     */
    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    /**
     * Provê a implementação do [AuthRepository].
     *
     * @Binds é usado para dizer ao Hilt qual implementação usar
     * quando uma interface for solicitada.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    // Deixaremos o resto vazio por enquanto.
    // Nas próximas Sprints, adicionaremos aqui os provedores
    // para o Room Database, DataStore, etc.
}