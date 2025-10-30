package com.kingcreationslabs.autocusto.domain.use_case.validation

//import android.util.Patterns // Esta é uma das poucas exceções de 'import android' no domain, pois é uma lógica de plataforma
import javax.inject.Inject

/**
 * Caso de Uso para validar um endereço de e-mail.
 * (Camada de Domínio)
 */
class ValidateEmailUseCase @Inject constructor() { // @Inject para Hilt poder prover

    /**
     * Executa o caso de uso.
     * @param email O e-mail a ser validado.
     * @return [ValidationResult] O resultado da validação.
     */
    // Esta é a Regex oficial usada pela classe Patterns.EMAIL_ADDRESS do Android,
    // mas agora definida como uma constante pura de Kotlin, tornando-a testável.
    private val emailRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    /**
     * Executa o caso de uso.
     * @param email O e-mail a ser validado.
     * @return [ValidationResult] O resultado da validação.
     */
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "O e-mail não pode estar vazio."
            )
        }
        // CORRIGIDO: Agora usamos a nossa Regex local
        if (!emailRegex.matches(email)) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Formato de e-mail inválido."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}