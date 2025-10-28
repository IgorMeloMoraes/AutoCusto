package com.kingcreationslabs.autocusto.presentation.navigation

/**
 * Objeto selado (sealed object) para definir as rotas de navegação
 * de forma centralizada e segura (type-safe).
 */
sealed class Routes(val route: String) {
    object Splash : Routes("splash_screen")
    object Onboarding : Routes("onboarding_screen")
    object Login : Routes("login_screen")
    object MainApp : Routes("main_app_graph") // Esta será a rota para o app "logado"
}