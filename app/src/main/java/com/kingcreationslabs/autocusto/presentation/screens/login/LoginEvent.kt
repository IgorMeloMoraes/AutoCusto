package com.kingcreationslabs.autocusto.presentation.screens.login

/**
 * Define todas as ações (eventos/intenções) que o utilizador
 * pode realizar na [LoginScreen].
 * (Camada de Presentation)
 */
sealed interface LoginEvent {
    /**
     * Evento disparado sempre que o campo de e-mail muda.
     * @property email O novo texto do campo.
     */
    data class EmailChanged(val email: String) : LoginEvent

    /**
     * Evento disparado sempre que o campo de senha muda.
     * @property password O novo texto do campo.
     */
    data class PasswordChanged(val password: String) : LoginEvent

    /**
     * Evento disparado quando o botão principal de Login (E-mail/Senha) é clicado.
     */
    object LoginClicked : LoginEvent

    // TODO: Tarefa 2.8 - Evento para o botão de Login com Google.
    // object GoogleSignInClicked : LoginEvent

    // TODO: Tarefa 2.9 - Evento para o botão de Login com Facebook.
    // object FacebookSignInClicked : LoginEvent
}