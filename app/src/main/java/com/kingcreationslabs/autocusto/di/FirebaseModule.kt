package com.kingcreationslabs.autocusto.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para prover instâncias de serviços do Firebase.
 *
 * Usamos um 'object' e @Provides pois estamos provendo classes externas
 * (do SDK do Firebase) que não podemos injetar via construtor (@Inject).
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provê uma instância Singleton do [FirebaseAuth].
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Futuramente (Sprint 3), adicionaremos o provideFirestore() aqui.
}