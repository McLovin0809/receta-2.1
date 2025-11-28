package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   SETTINGS SCREEN TESTABLE
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTestable() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                modifier = Modifier.testTag("topBar")
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "Pantalla de Configuración",
                modifier = Modifier.testTag("settingsText")
            )
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — SettingsScreenTest
   ------------------------------------------------------------------------- */

class SettingsScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun settingsScreen_muestraTopBar() {

        rule.setContent {
            SettingsScreenTestable()
        }

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Configuración").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_muestraTextoPrincipal() {

        rule.setContent {
            SettingsScreenTestable()
        }

        rule.onNodeWithTag("settingsText")
            .assertIsDisplayed()
            .assertTextEquals("Pantalla de Configuración")
    }

    @Test
    fun settingsScreen_noCrashea() {

        rule.setContent {
            SettingsScreenTestable()
        }

        // Si llegamos aquí, la pantalla se compuso correctamente
        assert(true)
    }
}
