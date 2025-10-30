package com.kingcreationslabs.autocusto.presentation.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kingcreationslabs.autocusto.R
import com.kingcreationslabs.autocusto.presentation.navigation.Routes

/**
 * Tela de Registro.
 *
 * Esta é a "View" (Composable) que exibe o estado do [RegisterViewModel]
 * e envia [RegisterEvent]s para ele.
 *
 * @param navController Controlador de navegação (provido pelo AppNavigation)
 * @param viewModel O [RegisterViewModel] injetado pelo Hilt
 */
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // 1. Coleta o estado da UI do ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estado local da UI para controlar a visibilidade da senha
    var passwordVisible by remember { mutableStateOf(false) }

    // 2. Coleta os eventos de navegação (Side-Effects)
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { route ->
            // Evento recebido! Navega e limpa a pilha
            navController.navigate(route.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    // --- A UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- Campo de E-mail ---
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { email ->
                    viewModel.onEvent(RegisterEvent.EmailChanged(email))
                },
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { Text(text = it) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Senha ---
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { password ->
                    viewModel.onEvent(RegisterEvent.PasswordChanged(password))
                },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Senha") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    val painter = if (passwordVisible)
                        painterResource(id = R.drawable.ic_visibility)
                    else
                        painterResource(id = R.drawable.ic_visibility_off)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painter, contentDescription = "Mostrar/Esconder senha")
                    }
                },
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { Text(text = it) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Confirmar Senha ---
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { confirmPassword ->
                    viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged(confirmPassword))
                },
                label = { Text("Confirmar Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirmar Senha") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = uiState.confirmPasswordError != null,
                supportingText = {
                    uiState.confirmPasswordError?.let { Text(text = it) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Erro Geral ---
            uiState.generalError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- Botão de Registrar ---
            Button(
                onClick = {
                    viewModel.onEvent(RegisterEvent.RegisterClicked)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cadastrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Botão "Voltar para Login" ---
            TextButton(
                onClick = {
                    // Ação de navegação simples: apenas volta para a tela anterior (Login)
                    navController.popBackStack()
                }
            ) {
                Text("Já tem uma conta? Faça login")
            }
        }

        // --- Indicador de Loading ---
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}