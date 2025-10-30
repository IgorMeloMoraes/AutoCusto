package com.kingcreationslabs.autocusto.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.kingcreationslabs.autocusto.domain.use_case.auth.RegisterWithEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidateConfirmPasswordUseCase
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
 * ViewModel para a [RegisterScreen].
 *
 * Responsável por gerir o estado da UI (RegisterUiState) e processar
 * as ações do utilizador (RegisterEvent), utilizando os UseCases da camada de domínio.
 *
 * @property validateEmailUseCase Caso de Uso para validar o e-mail.
 * @property validatePasswordUseCase Caso de Uso para validar a senha.
 * @property validateConfirmPasswordUseCase Caso de Uso para validar a confirmação de senha.
 * @property registerWithEmailUseCase Caso de Uso para executar o registro com e-mail/senha.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    // Injeta os UseCases que criamos na Tarefa 2.3
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase
) : ViewModel() {

    // Estado interno mutável da UI
    private val _uiState = MutableStateFlow(RegisterUiState())
    // Estado público e imutável da UI, exposto para o Composable
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Canal para enviar eventos de navegação (Side-Effects) para a UI
    private val _navigationEvent = Channel<Routes>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    /**
     * Ponto de entrada único para todos os eventos da UI.
     * @param event O [RegisterEvent] disparado pela UI.
     */
    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> onEmailChanged(event.email)
            is RegisterEvent.PasswordChanged -> onPasswordChanged(event.password)
            is RegisterEvent.ConfirmPasswordChanged -> onConfirmPasswordChanged(event.confirmPassword)
            RegisterEvent.RegisterClicked -> onRegisterClicked()
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
                emailError = validationResult.errorMessage
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
                passwordError = validationResult.errorMessage
            )
        }
    }

    /**
     * Chamado quando o campo de confirmação de senha muda.
     * Atualiza o estado e executa a validação em tempo real.
     */
    private fun onConfirmPasswordChanged(confirmPassword: String) {
        // Validação em tempo real (se a senha já foi digitada)
        val validationResult = validateConfirmPasswordUseCase(
            password = _uiState.value.password,
            confirmPassword = confirmPassword
        )
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = validationResult.errorMessage
            )
        }
    }

    /**
     * Chamado quando o botão de Registro é clicado.
     * Executa a validação final em todos os campos e tenta o registro.
     */
    private fun onRegisterClicked() {
        // 1. Executa a validação final em todos os campos
        val emailResult = validateEmailUseCase(_uiState.value.email)
        val passwordResult = validatePasswordUseCase(_uiState.value.password)
        val confirmPasswordResult = validateConfirmPasswordUseCase(
            password = _uiState.value.password,
            confirmPassword = _uiState.value.confirmPassword
        )

        // 2. Verifica se há algum erro de validação
        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { !it.isSuccess }

        if (hasError) {
            // Atualiza o estado para exibir todos os erros
            _uiState.update {
                it.copy(
                    emailError = emailResult.errorMessage,
                    passwordError = passwordResult.errorMessage,
                    confirmPasswordError = confirmPasswordResult.errorMessage
                )
            }
            return // Para a execução
        }

        // 3. Se a validação passou, inicia o registro
        viewModelScope.launch {
            // Define o estado de carregamento
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            // 4. Chama o UseCase (que chama o Repositório)
            val registerResult = registerWithEmailUseCase(
                email = _uiState.value.email,
                pass = _uiState.value.password
            )

            // 5. Trata o resultado
            registerResult.onSuccess {
                // Sucesso! Remove o loading
                _uiState.update { it.copy(isLoading = false) }
                // Envia o evento de navegação para a UI
                _navigationEvent.send(Routes.MainApp)
            }
            registerResult.onFailure { exception ->
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
     */
    private fun mapAuthExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is FirebaseAuthUserCollisionException -> "Este e-mail já está em uso."
            else -> "Ocorreu um erro. Tente novamente."
        }
    }
}