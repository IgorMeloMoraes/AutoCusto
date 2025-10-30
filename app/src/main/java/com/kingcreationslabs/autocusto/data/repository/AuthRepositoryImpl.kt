package com.kingcreationslabs.autocusto.data.repository

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kingcreationslabs.autocusto.domain.model.AuthUser
import com.kingcreationslabs.autocusto.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // <--- ESSA IMPORTAÇÃO NÃO SERÁ MAIS USADA, PODE APAGAR
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// IMPORTAÇÕES NOVAS E CORRIGIDAS
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementação concreta do [AuthRepository] que utiliza o Firebase Authentication.
 * (Camada de Dados)
 *
 * @property auth A instância do [FirebaseAuth] injetada pelo Hilt (provida pelo [FirebaseModule]).
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    /**
     * Mapeia o estado de autenticação do Firebase para um Flow de domínio [AuthUser].
     *
     * Devido à depreciação das extensões KTX (authStateChanges) no BOM 34.0.0+,
     * usamos um [callbackFlow] para envelopar o clássico [addAuthStateListener].
     */
    override fun getAuthState(): Flow<AuthUser?> {
        // callbackFlow é o construtor de Flow ideal para APIs baseadas em listeners.
        return callbackFlow {
            // 1. Cria o listener do Firebase Auth
            val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                // 2. Mapeia o resultado (FirebaseUser) para o nosso AuthUser
                val authUser = firebaseAuth.currentUser?.toAuthUser()

                // 3. Emite o novo valor para o Flow
                trySend(authUser)
            }

            // 4. Adiciona o listener ao cliente do Firebase
            auth.addAuthStateListener(authListener)

            // 5. Bloco 'awaitClose' (crucial)
            // Este bloco é chamado quando o Flow é cancelado (ex: o ViewModel é destruído).
            // Ele remove o listener para evitar memory leaks.
            awaitClose {
                auth.removeAuthStateListener(authListener)
            }
        }
    }

    /**
     * Tenta login com Email/Senha usando o SDK do Firebase.
     * O .await() (da dependência kotlinx-coroutines-play-services) suspende
     * a coroutine até que a tarefa do Firebase seja concluída.
     */
    override suspend fun signInWithEmail(email: String, pass: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(Unit)
        } catch (e: Exception) {
            // Captura exceções (ex: FirebaseAuthInvalidCredentialsException, etc)
            Result.failure(e)
        }
    }

    /**
     * Tenta registro com Email/Senha usando o SDK do Firebase.
     */
    override suspend fun registerWithEmail(email: String, pass: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            Result.success(Unit)
        } catch (e: Exception) {
            // Captura exceções (ex: FirebaseAuthUserCollisionException)
            Result.failure(e)
        }
    }

    /**
     * Tenta login com Credencial do Google.
     */
    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Tenta login com Credencial do Facebook.
     */
    override suspend fun signInWithFacebook(accessToken: String): Result<Unit> {
        return try {
            val credential = FacebookAuthProvider.getCredential(accessToken)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Desloga o usuário da sessão local do Firebase.
     */
    override suspend fun logout() {
        auth.signOut()
    }

    /**
     * Função de extensão (helper) privada para mapear o modelo de dados do Firebase
     * ([FirebaseUser]) para o nosso modelo de domínio [AuthUser].
     */
    private fun FirebaseUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = this.uid,
            email = this.email,
            displayName = this.displayName
        )
    }
}