package com.kingcreationslabs.autocusto.presentation.screens.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kingcreationslabs.autocusto.R // Importe o R
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import kotlinx.coroutines.launch

/**
 * Tela de Onboarding (Carrossel).
 * Apresenta as funcionalidades do app ao utilizador pela primeira vez.
 *
 * @param navController Controlador de navegação para ir para o Login.
 */
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel() // <-- Injeta o ViewModel
) {
    // TODO: Substitua pelos seus próprios recursos
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.ic_launcher_foreground, // TODO: Troque pela sua imagem
            title = "Bem-vindo ao AutoCusto",
            description = "Calcule os seus gastos com combustível de forma fácil e rápida."
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_launcher_foreground, // TODO: Troque pela sua imagem
            title = "Etanol ou Gasolina?",
            description = "Descubra qual combustível é mais vantajoso para o seu veículo."
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_launcher_foreground, // TODO: Troque pela sua imagem
            title = "Planeie as Suas Viagens",
            description = "Saiba exatamente quanto vai gastar antes de pegar a estrada."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // O Carrossel de Páginas
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f) // Ocupa a maior parte da tela
        ) { pageIndex ->
            OnboardingPageItem(page = pages[pageIndex])
        }

        // Indicadores de Página (as "bolinhas")
        PagerIndicator(
            pagerState = pagerState,
            pageCount = pages.size,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )

        // Botão de Ação (Próximo / Concluir)
        ActionButton(
            pagerState = pagerState,
            pageCount = pages.size,
            onNextClicked = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onDoneClicked = {
                //  A lógica de negocio agora é chamada aqui
                viewModel.setOnboardingCompleted()

                // Navega para o Login e limpa a pilha de navegação
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Onboarding.route) {
                        inclusive = true
                    }
                    // Garante que a Splash não está na pilha
                    popUpTo(Routes.Splash.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

/**
 * Composable para exibir uma única página do carrossel.
 */
@Composable
private fun OnboardingPageItem(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier.size(200.dp) // Ajuste o tamanho conforme necessário
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Composable para exibir os indicadores de página (bolinhas).
 */
@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { index ->
            val color = if (pagerState.currentPage == index) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/**
 * Composable para o botão de ação (Próximo/Concluir).
 */
@Composable
private fun ActionButton(
    pagerState: PagerState,
    pageCount: Int,
    onNextClicked: () -> Unit,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLastPage = pagerState.currentPage == pageCount - 1

    Button(
        onClick = {
            if (isLastPage) {
                onDoneClicked()
            } else {
                onNextClicked()
            }
        },
        modifier = modifier.height(50.dp)
    ) {
        val text = if (isLastPage) "Concluir" else "Próximo"
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}