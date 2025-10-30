package com.kingcreationslabs.autocusto.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingcreationslabs.autocusto.domain.use_case.auth.SignInWithEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidateEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidatePasswordUseCase
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a [LoginScreen].
 *
 * Responsável por gerir o estado da UI (LoginUiState) e processar
 * as ações do utilizador (LoginEvent), utilizando os UseCases da camada de domínio.
 *
 * @property validateEmailUseCase Caso de Uso para validar o e-mail.
 * @property validatePasswordUseCase Caso de Uso para validar a senha.
 * @property signInWithEmailUseCase Caso de Uso para executar o login com e-mail/senha.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    // Injeta os UseCases que criamos na Tarefa 2.3
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase
    // TODO: Injetar os UseCases de Google/Facebook nas Tarefas 2.8/2.9
) : ViewModel() {

    // Estado interno mutável da UI
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado público e imutável da UI, exposto para o Composable
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Canal para enviar eventos de navegação (Side-Effects) para a UI
    private val _navigationEvent = Channel<Routes>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    /**
     * Ponto de entrada único para todos os eventos da UI.
     * @param event O [LoginEvent] disparado pela UI.
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> onEmailChanged(event.email)
            is LoginEvent.PasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.LoginClicked -> onLoginClicked()
            // TODO: Adicionar os 'when' para Google/Facebook
        }
    }

    /**
     * Chamado quando o campo de e-mail muda.
     * Atualiza o estado e executa a validação em tempo real.
     */
    private fun onEmailChanged(email: String) {
        val validationResult = validateEmailUseCase(email)
        _uiState.update {
            it.copy(
                email = email,
                emailError = validationResult.errorMessage // Nulo se for sucesso
            )
        }
    }

    /**
     * Chamado quando o campo de senha muda.
     * Atualiza o estado e executa a validação em tempo real.
     */
    private fun onPasswordChanged(password: String) {
        val validationResult = validatePasswordUseCase(password)
        _uiState.update {
            it.copy(
                password = password,
                passwordError = validationResult.errorMessage // Nulo se for sucesso
            )
        }
    }

    /**
     * Chamado quando o botão de Login é clicado.
     * Executa a validação final e tenta o login.
     */
    private fun onLoginClicked() {
        // 1. Executa a validação final em ambos os campos
        val emailResult = validateEmailUseCase(_uiState.value.email)
        val passwordResult = validatePasswordUseCase(_uiState.value.password)

        // 2. Verifica se há algum erro de validação
        val hasError = listOf(emailResult, passwordResult).any { !it.isSuccess }

        if (hasError) {
            // Atualiza o estado para exibir os erros (caso o utilizador não tenha desfocado os campos)
            _uiState.update {
                it.copy(
                    emailError = emailResult.errorMessage,
                    passwordError = passwordResult.errorMessage
                )
            }
            return // Para a execução
        }

        // 3. Se a validação passou, inicia o login
        viewModelScope.launch {
            // Define o estado de carregamento
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            // 4. Chama o UseCase (que chama o Repositório)
            val loginResult = signInWithEmailUseCase(
                email = _uiState.value.email,
                pass = _uiState.value.password
            )

            // 5. Trata o resultado
            loginResult.onSuccess {
                // Sucesso! Remove o loading
                _uiState.update { it.copy(isLoading = false) }
                // Envia o evento de navegação para a UI
                _navigationEvent.send(Routes.MainApp)
            }
            loginResult.onFailure { exception ->
                // Falha! Remove o loading e define o erro geral
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = mapAuthExceptionToMessage(exception)
                    )
                }
            }
        }
    }

    /**
     * Mapeia Exceções do Firebase para mensagens amigáveis.
     * (Esta é uma lógica de apresentação, por isso vive no ViewModel)
     */
    private fun mapAuthExceptionToMessage(exception: Throwable): String {
        // NOTA: Para ser 100% preciso, deveríamos importar e checar os códigos
        // de FirebaseAuthInvalidUserException, FirebaseAuthInvalidCredentialsException, etc.
        // Por agora, uma mensagem genérica é suficiente.
        return when (exception) {
            // TODO: Mapear exceções específicas (ex: email não encontrado, senha errada)
            else -> "E-mail ou senha inválidos. Tente novamente."
        }
    }
}