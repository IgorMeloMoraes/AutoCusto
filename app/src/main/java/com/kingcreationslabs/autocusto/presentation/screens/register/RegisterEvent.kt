package com.kingcreationslabs.autocusto.presentation.screens.register

/**
 * Define todas as ações (eventos/intenções) que o utilizador
 * pode realizar na [RegisterScreen].
 * (Camada de Presentation)
 */
sealed interface RegisterEvent {
    /**
     * Evento disparado sempre que o campo de e-mail muda.
     * @property email O novo texto do campo.
     */
    data class EmailChanged(val email: String) : RegisterEvent

    /**
     * Evento disparado sempre que o campo de senha muda.
     * @property password O novo texto do campo.
     */
    data class PasswordChanged(val password: String) : RegisterEvent

    /**
     * Evento disparado sempre que o campo de confirmação de senha muda.
     * @property confirmPassword O novo texto do campo.
     */
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent

    /**
     * Evento disparado quando o botão principal de Registro é clicado.
     */
    object RegisterClicked : RegisterEvent
}