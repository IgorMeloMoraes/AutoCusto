package com.kingcreationslabs.autocusto.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Módulo Hilt que provê dependências em escopo de
 * Singleton (nível de Application).
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Deixaremos vazio por enquanto.
    // Nas próximas Sprints, adicionaremos aqui os provedores para o Room Database, DataStore, e os Repositórios.
    // Exemplo futuro:
    // @Singleton
    // @Provides
    // fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    //     return Room.databaseBuilder(...).build()
    // }

}