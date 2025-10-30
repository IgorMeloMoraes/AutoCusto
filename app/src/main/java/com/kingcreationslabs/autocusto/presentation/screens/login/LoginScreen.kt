package com.kingcreationslabs.autocusto.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import com.kingcreationslabs.autocusto.R

/**
 * Tela de Login.
 *
 * Esta é a "View" (Composable) que exibe o estado do [LoginViewModel]
 * e envia [LoginEvent]s para ele.
 *
 * @param navController Controlador de navegação (provido pelo AppNavigation)
 * @param viewModel O [LoginViewModel] injetado pelo Hilt
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // 1. Coleta o estado da UI do ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estado local da UI para controlar a visibilidade da senha
    var passwordVisible by remember { mutableStateOf(false) }

    // 2. Coleta os eventos de navegação (Side-Effects)
    // Este LaunchedEffect "escuta" o Channel de navegação.
    LaunchedEffect(key1 = true) { // key1 = true significa que só executa uma vez
        viewModel.navigationEvent.collect { route ->
            // Evento recebido! Navega e limpa a pilha
            navController.navigate(route.route) {
                // Limpa toda a pilha de back stack (Splash, Onboarding, Login)
                // para que o utilizador não possa "voltar" para o login.
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
                text = "Login",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- Campo de E-mail ---
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { email ->
                    // 3. Envia o evento de mudança para o ViewModel
                    viewModel.onEvent(LoginEvent.EmailChanged(email))
                },
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = uiState.emailError != null, // Mostra erro se não for nulo
                supportingText = {
                    uiState.emailError?.let { error ->
                        Text(text = error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Senha ---
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { password ->
                    // 3. Envia o evento de mudança para o ViewModel
                    viewModel.onEvent(LoginEvent.PasswordChanged(password))
                },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Senha") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val painter = if (passwordVisible)
                        painterResource(id = R.drawable.ic_visibility) // Usando o Vector Asset
                    else
                        painterResource(id = R.drawable.ic_visibility_off) // Usando o Vector Asset

                    val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painter, contentDescription = description)
                    }
                },
                isError = uiState.passwordError != null, // Mostra erro se não for nulo
                supportingText = {
                    uiState.passwordError?.let { error ->
                        Text(text = error)
                    }
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

            // --- Botão de Login ---
            Button(
                onClick = {
                    // 3. Envia o evento de clique para o ViewModel
                    viewModel.onEvent(LoginEvent.LoginClicked)
                },
                enabled = !uiState.isLoading, // Desativa o botão enquanto carrega
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }

            // TODO: Botões de Google e Facebook (Tarefas 2.8 e 2.9)
        }

        // --- Indicador de Loading ---
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}