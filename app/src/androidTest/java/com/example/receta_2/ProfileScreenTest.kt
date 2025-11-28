package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   TESTABLE PROFILE SCREEN
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTestable(
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = { Text("Mi Perfil", modifier = Modifier.testTag("perfilTitle")) }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Información del Usuario",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag("infoUsuario")
            )

            Button(
                onClick = onSettingsClick,
                modifier = Modifier.fillMaxWidth().testTag("btnSettings")
            ) {
                Text("Configuración")
            }

            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth().testTag("btnLogout"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — ProfileScreenTest
   ------------------------------------------------------------------------- */

class ProfileScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private var settingsClicked = false
    private var logoutClicked = false

    private fun setContent() {
        settingsClicked = false
        logoutClicked = false

        rule.setContent {
            ProfileScreenTestable(
                onSettingsClick = { settingsClicked = true },
                onLogoutClick = { logoutClicked = true }
            )
        }
    }

    /* --- TOP BAR --- */

    @Test
    fun profileScreen_muestraTopBar() {
        setContent()

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithTag("perfilTitle").assertTextContains("Mi Perfil")
    }

    /* --- INFORMACIÓN DEL USUARIO --- */

    @Test
    fun profileScreen_muestraInformacionUsuario() {
        setContent()

        rule.onNodeWithTag("infoUsuario")
            .assertIsDisplayed()
            .assertTextContains("Información del Usuario")
    }

    /* --- BOTÓN CONFIGURACIÓN --- */

    @Test
    fun profileScreen_botonSettingsFunciona() {
        setContent()

        rule.onNodeWithTag("btnSettings").performClick()

        assert(settingsClicked)
    }

    /* --- BOTÓN LOGOUT --- */

    @Test
    fun profileScreen_botonLogoutFunciona() {
        setContent()

        rule.onNodeWithTag("btnLogout").performClick()

        assert(logoutClicked)
    }

    /* --- ESTRUCTURA GENERAL --- */

    @Test
    fun profileScreen_muestraBotonesCorrectamente() {
        setContent()

        rule.onNodeWithTag("btnSettings").assertIsDisplayed()
        rule.onNodeWithTag("btnLogout").assertIsDisplayed()
    }
}