package com.star.forgekitdemo

import androidx.compose.ui.window.ComposeUIViewController

@Suppress("FunctionName") // Exported Swift factory follows UIKit naming conventions.
fun MainViewController() =
    ComposeUIViewController {
        ForgeKitDemoApp()
    }
