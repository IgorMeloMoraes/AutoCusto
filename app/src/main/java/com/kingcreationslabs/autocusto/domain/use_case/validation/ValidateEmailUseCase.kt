package com.kingcreationslabs.autocusto.domain.use_case.validation

import android.util.Patterns // Esta é uma das poucas exceções de 'import android' no domain, pois é uma lógica de plataforma
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
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "O e-mail não pode estar vazio."
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Formato de e-mail inválido."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}