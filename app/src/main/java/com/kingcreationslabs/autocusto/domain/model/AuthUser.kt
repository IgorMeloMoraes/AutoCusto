package com.kingcreationslabs.autocusto.domain.model

/**
 * Representa um usuário autenticado na camada de domínio (Clean Architecture).
 *
 * Esta é uma representação agnóstica, não contém nenhuma referência
 * a classes do Firebase (como FirebaseUser).
 *
 * @property uid O ID único do usuário (vindo do provedor de auth).
 * @property email O email do usuário (pode ser nulo, ex: login anônimo ou social).
 * @property displayName O nome de exibição (pode ser nulo).
 */
data class AuthUser(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null
    // Futuramente, podemos adicionar photoUrl: String?
)