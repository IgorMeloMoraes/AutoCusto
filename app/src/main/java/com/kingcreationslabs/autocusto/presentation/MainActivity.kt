package com.kingcreationslabs.autocusto.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kingcreationslabs.autocusto.presentation.navigation.AppNavigation // <-- Importa a Navegação
import com.kingcreationslabs.autocusto.presentation.theme.AutoCustoTheme // Importa o Tema
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aqui aplicamos o nosso tema
            AutoCustoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AQUI ESTÁ A MUDANÇA:
                    // Estamos carregarregando o nosso grafo de navegação
                    AppNavigation()
                }
            }
        }
    }
}