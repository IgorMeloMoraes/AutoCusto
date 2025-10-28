package com.kingcreationslabs.autocusto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// A anotação @HiltAndroidApp faz toda a mágica de inicializar o Hilt.
@HiltAndroidApp
class AutoCustoApp : Application() {
    // Por enquanto, esta classe fica vazia.
}