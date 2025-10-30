package com.kingcreationslabs.autocusto.domain.use_case.validation

/**
 * Representa o resultado de uma operação de validação de campo (ex: email, senha).
 * (Camada de Domínio)
 *
 * @property isSuccess Se a validação foi bem-sucedida.
 * @property errorMessage A mensagem de erro (nula se [isSuccess] for true).
 */
data class ValidationResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)