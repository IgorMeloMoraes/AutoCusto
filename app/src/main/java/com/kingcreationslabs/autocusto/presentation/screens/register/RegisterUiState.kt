package com.kingcreationslabs.autocusto.presentation.screens.register

/**
 * Representa o estado da UI para a [RegisterScreen].
 * (Camada de Presentation)
 *
 * @property email O valor atual no campo de e-mail.
 * @property emailError A mensagem de erro para o e-mail (nulo se válido).
 * @property password O valor atual no campo de senha.
 * @property passwordError A mensagem de erro para a senha (nulo se válido).
 * @property confirmPassword O valor atual no campo de confirmação de senha.
 * @property confirmPasswordError A mensagem de erro para a confirmação (nulo se válido).
 * @property isLoading Indica se uma operação de registro está em andamento.
 * @property generalError Uma mensagem de erro geral (ex: "E-mail já em uso").
 */
data class RegisterUiState(
    // Estado dos campos
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,

    // Estado da tela
    val isLoading: Boolean = false,
    val generalError: String? = null
)