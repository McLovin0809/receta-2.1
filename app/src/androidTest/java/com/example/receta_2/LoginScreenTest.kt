package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.receta_2.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAKE AUTH VIEWMODEL
   ------------------------------------------------------------------------- */

class FakeAuthVM(
    initLogged: Boolean = false,
    initError: String? = null
) : AuthViewModel() {

    private val _errorMessage = MutableStateFlow(initError)
     override val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(initLogged)
     override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun emitError(msg: String) {
        _errorMessage.value = msg
    }

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }
}

/* -------------------------------------------------------------------------
   TESTABLE LOGIN SCREEN
   ------------------------------------------------------------------------- */

@Composable
fun LoginScreenTestable(
    viewModel: FakeAuthVM,
    onLoginSuccess: (String, String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {},
    onNavigateHome: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#\$%^&+=!]{6,}$")

    val emailValid = emailRegex.matches(email)
    val passValid = passwordRegex.matches(password)

    val formValid = email.isNotBlank() && password.isNotBlank() && emailValid && passValid

    if (isLoggedIn) {
        LaunchedEffect(true) { onNavigateHome() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Iniciar Sesión", modifier = Modifier.testTag("title"))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            isError = email.isNotEmpty() && !emailValid,
            modifier = Modifier.fillMaxWidth().testTag("inputEmail")
        )
        if (!emailValid && email.isNotEmpty()) {
            Text("Correo inválido", color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("errorEmail"))
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && !passValid,
            modifier = Modifier.fillMaxWidth().testTag("inputPassword")
        )
        if (!passValid && password.isNotEmpty()) {
            Text(
                "Debe tener al menos 6 caracteres y contener letras y números",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("errorPassword")
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onLoginSuccess(email, password) },
            enabled = formValid,
            modifier = Modifier.fillMaxWidth().testTag("btnLogin")
        ) { Text("Entrar") }

        if (!errorMessage.isNullOrEmpty()) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("errorVM"))
        }

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.testTag("btnRegister")
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS
   ------------------------------------------------------------------------- */

class LoginScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private lateinit var vm: FakeAuthVM
    private var loginEmail = ""
    private var loginPass = ""
    private var registerClicked = false
    private var navigatedHome = false

    private fun setContent() {
        loginEmail = ""
        loginPass = ""
        registerClicked = false
        navigatedHome = false

        vm = FakeAuthVM()

        rule.setContent {
            LoginScreenTestable(
                viewModel = vm,
                onLoginSuccess = { e, p ->
                    loginEmail = e
                    loginPass = p
                },
                onRegisterClick = { registerClicked = true },
                onNavigateHome = { navigatedHome = true }
            )
        }
    }

    @Test
    fun login_muestraCamposPrincipales() {
        setContent()
        rule.onNodeWithTag("inputEmail").assertIsDisplayed()
        rule.onNodeWithTag("inputPassword").assertIsDisplayed()
        rule.onNodeWithTag("btnLogin").assertIsDisplayed()
    }

    @Test
    fun login_validaEmailInvalido() {
        setContent()
        rule.onNodeWithTag("inputEmail").performTextInput("correo_mal")
        rule.onNodeWithTag("errorEmail").assertIsDisplayed()
    }

    @Test
    fun login_validaPasswordInvalida() {
        setContent()
        rule.onNodeWithTag("inputPassword").performTextInput("abcd")
        rule.onNodeWithTag("errorPassword").assertIsDisplayed()
    }

    @Test
    fun login_botonDeshabilitadoConFormularioInvalido() {
        setContent()
        rule.onNodeWithTag("btnLogin").assertIsNotEnabled()
    }

    @Test
    fun login_botonHabilitadoConFormularioValido() {
        setContent()

        rule.onNodeWithTag("inputEmail").performTextInput("test@mail.com")
        rule.onNodeWithTag("inputPassword").performTextInput("abc123")

        rule.onNodeWithTag("btnLogin").assertIsEnabled()
    }

    @Test
    fun login_onLoginSuccessSeEjecuta() {
        setContent()

        rule.onNodeWithTag("inputEmail").performTextInput("user@mail.com")
        rule.onNodeWithTag("inputPassword").performTextInput("abc123")

        rule.onNodeWithTag("btnLogin").performClick()

        assert(loginEmail == "user@mail.com")
        assert(loginPass == "abc123")
    }

    @Test
    fun login_muestraErrorDelViewModel() {
        setContent()
        vm.emitError("Credenciales incorrectas")

        rule.onNodeWithTag("errorVM").assertTextContains("Credenciales incorrectas")
    }

    @Test
    fun login_navegaCuandoIsLoggedInEsTrue() {
        setContent()
        vm.setLoggedIn(true)

        assert(navigatedHome)
    }

    @Test
    fun login_registerClickFunciona() {
        setContent()

        rule.onNodeWithTag("btnRegister").performClick()
        assert(registerClicked)
    }
}