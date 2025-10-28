package com.kingcreationslabs.autocusto.presentation.screens.onboarding

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import com.kingcreationslabs.autocusto.presentation.theme.AutoCustoTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kingcreationslabs.autocusto.presentation.screens.LoginScreen // O placeholder

/**
 * Teste de UI (Instrumentado) para a [OnboardingScreen].
 *
 * Este teste verifica as interações do utilizador (cliques, deslizes)
 * e a lógica de navegação.
 */
class OnboardingScreenTest {

    // Regra de teste do Compose. Usamos createComposeRule() pois não precisamos
    // de uma Activity real para este Composable.
    @get:Rule
    val composeTestRule = createComposeRule()

    // Controlador de navegação falso (TestNavHostController)
    private lateinit var navController: TestNavHostController

    // Prepara o ambiente ANTES de cada teste
    @Before
    fun setUp() {
        // Inicializa o controlador de navegação de teste
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        // Define o conteúdo da UI a ser testado
        composeTestRule.setContent {
            // Envolvemos a nossa tela no tema para que ela possa
            // aceder a MaterialTheme.colorScheme, etc.
            AutoCustoTheme {
                // Precisamos de um NavHost real para que o
                // navController saiba para onde navegar.
                NavHost(
                    navController = navController,
                    startDestination = Routes.Onboarding.route // Começa na tela que queremos testar
                ) {

                    // A nossa tela de teste
                    composable(route = Routes.Onboarding.route) {
                        OnboardingScreen(navController = navController)
                    }

                    // O destino para onde o teste 4 tenta navegar
                    composable(route = Routes.Login.route) {
                        // Usamos o placeholder "dummy"
                        // (o teste só precisa que a rota exista no "mapa")
                        LoginScreen()
                    }
                }
            }
        }
    }

    @Test
    fun `estadoInicial_MostraPaginaUmEBotaoProximo`() {
        // --- Verificação (Assert) ---
        // Verifica se o título da primeira página é exibido
        composeTestRule.onNodeWithText("Bem-vindo ao AutoCusto").assertIsDisplayed()

        // Verifica se o botão mostra "Próximo"
        composeTestRule.onNodeWithText("Próximo").assertIsDisplayed()
    }

    @Test
    fun `botaoProximo_NavegaPelasPaginasETextoMudaParaConcluir`() {
        // --- Ação (Act) ---
        // Clica em "Próximo" (vai para a página 2)
        composeTestRule.onNodeWithText("Próximo").performClick()

        // --- Verificação (Assert) ---
        // Espera a animação e verifica se a página 2 é exibida
        composeTestRule.waitForIdle() // Espera a animação do pager
        composeTestRule.onNodeWithText("Etanol ou Gasolina?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Próximo").assertIsDisplayed() // Botão ainda é "Próximo"

        // --- Ação (Act) ---
        // Clica em "Próximo" (vai para a página 3 - a última)
        composeTestRule.onNodeWithText("Próximo").performClick()

        // --- Verificação (Assert) ---
        // Espera a animação e verifica se a página 3 é exibida
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Planeie as Suas Viagens").assertIsDisplayed()

        // O botão AGORA deve ser "Concluir"
        composeTestRule.onNodeWithText("Concluir").assertIsDisplayed()
    }

    @Test
    fun `deslizar_NavegaParaProximaPagina`() {
        // --- Ação (Act) ---
        // Simula um deslize para a esquerda na tela
        composeTestRule.onNodeWithText("Bem-vindo ao AutoCusto").performTouchInput {
            swipeLeft()
        }

        // --- Verificação (Assert) ---
        // Espera a animação e verifica se a página 2 é exibida
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Etanol ou Gasolina?").assertIsDisplayed()
    }

    @Test
    fun `botaoConcluir_NavegaParaTelaDeLogin`() {
        // --- Preparação (Arrange) ---
        // Vai para a última página
        composeTestRule.onNodeWithText("Próximo").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Próximo").performClick()
        composeTestRule.waitForIdle()

        // Verifica se estamos na última página
        composeTestRule.onNodeWithText("Concluir").assertIsDisplayed()

        // --- Ação (Act) ---
        // Clica em "Concluir"
        composeTestRule.onNodeWithText("Concluir").performClick()

        // --- Verificação (Assert) ---
        // Verifica se a rota atual no controlador de navegação é a de Login
        val rotaAtual = navController.currentBackStackEntry?.destination?.route
        assertEquals(Routes.Login.route, rotaAtual)
    }
}