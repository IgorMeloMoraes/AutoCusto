package com.kingcreationslabs.autocusto.presentation.screens.login

/**
 * Representa o estado da UI para a [LoginScreen].
 * (Camada de Presentation)
 *
 * @property email O valor atual no campo de e-mail.
 * @property emailError A mensagem de erro para o e-mail (nulo se válido).
 * @property password O valor atual no campo de senha.
 * @property passwordError A mensagem de erro para a senha (nulo se válido).
 * @property isLoading Indica se uma operação de login está em andamento.
 * @property generalError Uma mensagem de erro geral (ex: "Usuário não encontrado").
 */
data class LoginUiState(
    // Estado dos campos
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,

    // Estado da tela
    val isLoading: Boolean = false,
    val generalError: String? = null
)