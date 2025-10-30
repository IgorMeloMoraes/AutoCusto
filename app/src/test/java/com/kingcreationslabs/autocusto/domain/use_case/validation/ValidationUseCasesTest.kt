package com.kingcreationslabs.autocusto.domain.use_case.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes de unidade para os Casos de Uso de Validação.
 */
class ValidationUseCasesTest {

    // Instâncias dos UseCases que estamos testando
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase

    @Before
    fun setUp() {
        // Como eles não têm dependências, podemos instanciá-los diretamente.
        validateEmailUseCase = ValidateEmailUseCase()
        validatePasswordUseCase = ValidatePasswordUseCase()
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()
    }

    // --- Testes para ValidateEmailUseCase ---

    @Test
    fun `ValidateEmailUseCase - e-mail vazio - retorna erro`() {
        val email = ""
        val result = validateEmailUseCase(email)
        assertFalse(result.isSuccess)
        assertEquals("O e-mail não pode estar vazio.", result.errorMessage)
    }

    @Test
    fun `ValidateEmailUseCase - e-mail mal formatado - retorna erro`() {
        val email = "teste.com"
        val result = validateEmailUseCase(email)
        assertFalse(result.isSuccess)
        assertEquals("Formato de e-mail inválido.", result.errorMessage)
    }

    @Test
    fun `ValidateEmailUseCase - e-mail válido - retorna sucesso`() {
        val email = "teste@teste.com"
        val result = validateEmailUseCase(email)
        assertTrue(result.isSuccess)
        assertNull(result.errorMessage)
    }

    // --- Testes para ValidatePasswordUseCase ---

    @Test
    fun `ValidatePasswordUseCase - senha vazia - retorna erro`() {
        val password = ""
        val result = validatePasswordUseCase(password)
        assertFalse(result.isSuccess)
        assertEquals("A senha não pode estar vazia.", result.errorMessage)
    }

    @Test
    fun `ValidatePasswordUseCase - senha curta - retorna erro`() {
        val password = "123"
        val result = validatePasswordUseCase(password)
        assertFalse(result.isSuccess)
        assertEquals("A senha deve ter pelo menos 6 caracteres.", result.errorMessage)
    }

    @Test
    fun `ValidatePasswordUseCase - senha válida - retorna sucesso`() {
        val password = "123456"
        val result = validatePasswordUseCase(password)
        assertTrue(result.isSuccess)
        assertNull(result.errorMessage)
    }

    // --- Testes para ValidateConfirmPasswordUseCase ---

    @Test
    fun `ValidateConfirmPasswordUseCase - senhas diferentes - retorna erro`() {
        val password = "123456"
        val confirmPassword = "654321"
        val result = validateConfirmPasswordUseCase(password, confirmPassword)
        assertFalse(result.isSuccess)
        assertEquals("As senhas não coincidem.", result.errorMessage)
    }

    @Test
    fun `ValidateConfirmPasswordUseCase - senhas iguais - retorna sucesso`() {
        val password = "123456"
        val confirmPassword = "123456"
        val result = validateConfirmPasswordUseCase(password, confirmPassword)
        assertTrue(result.isSuccess)
        assertNull(result.errorMessage)
    }
}