package com.kingcreationslabs.autocusto.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kingcreationslabs.autocusto.presentation.screens.LoginScreen
import com.kingcreationslabs.autocusto.presentation.screens.MainAppScreen
import com.kingcreationslabs.autocusto.presentation.screens.OnboardingScreen
import com.kingcreationslabs.autocusto.presentation.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    // Cria o controlador de navegação
    val navController = rememberNavController()

    // Cria o NavHost (o "contentor" que troca as telas)
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route // A primeira tela a ser mostrada
    ) {

        // Define cada tela e a qual rota ela pertence

        composable(route = Routes.Splash.route) {
            // Agora estamos a chamar a SplashScreen real e a passar o navController
            SplashScreen(navController = navController)
        }

        composable(route = Routes.Onboarding.route) {
            OnboardingScreen()
        }

        composable(route = Routes.Login.route) {
            LoginScreen()
        }

        composable(route = Routes.MainApp.route) {
            MainAppScreen()
        }

        // TODO Futuro: Quando o usuário estiver logado, podemos navegar para um grafo de navegação separado (um sub-grafo) para a área principal do app.
    }
}