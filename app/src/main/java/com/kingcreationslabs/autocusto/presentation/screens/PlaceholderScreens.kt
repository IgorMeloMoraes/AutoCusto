package com.kingcreationslabs.autocusto.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Usaremos estas telas temporariamente até implementarmos as telas reais nas próximas Sprints.



@Composable
fun LoginScreen() {
    DummyScreen(text = "Login/Register Screen")
}

@Composable
fun MainAppScreen() {
    DummyScreen(text = "Tela Principal (App Logado)")
}

@Composable
private fun DummyScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}