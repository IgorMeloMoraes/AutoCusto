package com.kingcreationslabs.autocusto.presentation.screens.splash

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kingcreationslabs.autocusto.R // Importe o seu R
import com.kingcreationslabs.autocusto.presentation.navigation.Routes

/**
 * Tela de Splash.
 *
 * Esta tela observa o [SplashViewModel] para determinar para onde navegar
 * após verificar o estado de onboarding do utilizador.
 *
 * @param navController Controlador de navegação para navegar para a próxima tela.
 * @param viewModel O ViewModel da splash screen, injetado pelo Hilt.
 */
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    // Observa o uiState do ViewModel de forma segura
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Efeito de animação para a logo (fade in/out)
    val infiniteTransition = rememberInfiniteTransition(label = "splash_alpha")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
        ),
        label = "splash_alpha_anim"
    )

    // Este LaunchedEffect irá "agir" quando o estado de isLoading mudar
    LaunchedEffect(key1 = uiState.isLoading) {
        // Se o carregamento terminou (isLoading é false)...
        if (!uiState.isLoading) {
            // ...decidimos a rota.
            val nextRoute = if (uiState.hasCompletedOnboarding) {
                // O utilizador já viu o onboarding, vai para o Login
                Routes.Login.route
            } else {
                // Primeira vez do utilizador, vai para o Onboarding
                Routes.Onboarding.route
            }

            // Navega para a próxima tela e remove a SplashScreen da pilha
            navController.navigate(nextRoute) {
                popUpTo(Routes.Splash.route) {
                    inclusive = true // Remove a SplashScreen para não se poder voltar a ela
                }
            }
        }
    }

    // --- A UI ---
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // A sua logo
        Image(
            painter = painterResource(id = R.drawable.logo_autocusto_splash),
            contentDescription = "Logo AutoCusto Splash",
            modifier = Modifier
                .size(250.dp)
                .alpha(alpha) // Aplica a animação de fade
        )
    }

    // O CircularProgressIndicator foi removido para uma UI mais limpa.
    // O uiState.isLoading ainda funciona em segundo plano
    // para controlar o LaunchedEffect de navegação.
}