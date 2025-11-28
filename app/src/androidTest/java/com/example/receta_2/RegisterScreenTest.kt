package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   VIEWMODEL FAKE PARA TEST
   ------------------------------------------------------------------------- */

class FakeAuthViewModel(
    private val errorFlow: MutableStateFlow<String?> = MutableStateFlow(null)
) {

    val errorMessage: StateFlow<String?> = errorFlow

    var registerCalled = false
    var receivedName = ""
    var receivedEmail = ""
    var receivedPassword = ""

    fun register(nombre: String, email: String, password: String, onSuccess: () -> Unit) {
        registerCalled = true
        receivedName = nombre
        receivedEmail = email
        receivedPassword = password
        onSuccess()
    }

    fun emitError(msg: String) {
        (errorMessage as MutableStateFlow).value = msg
    }
}

/* -------------------------------------------------------------------------
   REGISTER SCREEN TESTABLE
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenTestable(
    viewModel: FakeAuthViewModel,
    onNavigateLogin: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var passwordsMatch by remember { mutableStateOf(true) }

    val errorMessage by viewModel.errorMessage.collectAsState()

    val isFormValid by remember(username, email, password, confirmPassword) {
        derivedStateOf {
            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            passwordsMatch = password == confirmPassword
            username.isNotBlank() &&
                    email.isNotBlank() &&
                    password.length >= 6 &&
                    passwordsMatch &&
                    isEmailValid
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.testTag("btnBack")) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth().testTag("inputUsername")
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                isError = !isEmailValid && email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().testTag("inputEmail")
            )

            if (!isEmailValid && email.isNotEmpty()) {
                Text(
                    "Correo inválido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("errorEmail")
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                isError = password.isNotEmpty() && password.length < 6,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("inputPassword")
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                isError = !passwordsMatch && confirmPassword.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("inputConfirmPassword")
            )

            if (!passwordsMatch && confirmPassword.isNotEmpty()) {
                Text(
                    "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("errorPasswordMatch")
                )
            }

            Spacer(Modifier.height(16.dp))

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    errorMessage!!,
                    modifier = Modifier.testTag("errorViewModel"),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.register(
                        nombre = username,
                        email = email,
                        password = password
                    ) {
                        onNavigateLogin()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().testTag("btnRegister")
            ) {
                Text("Registrarme")
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — RegisterScreenTest
   ------------------------------------------------------------------------- */

class RegisterScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private lateinit var fakeVM: FakeAuthViewModel
    private var navigated = false

    private fun setContent() {
        fakeVM = FakeAuthViewModel()
        navigated = false
        rule.setContent {
            RegisterScreenTestable(
                viewModel = fakeVM,
                onNavigateLogin = { navigated = true }
            )
        }
    }

    @Test
    fun registerScreen_muestraCampos() {
        setContent()

        rule.onNodeWithTag("inputUsername").assertIsDisplayed()
        rule.onNodeWithTag("inputEmail").assertIsDisplayed()
        rule.onNodeWithTag("inputPassword").assertIsDisplayed()
        rule.onNodeWithTag("inputConfirmPassword").assertIsDisplayed()
        rule.onNodeWithTag("btnRegister").assertIsDisplayed()
    }

    @Test
    fun registerScreen_detectaEmailInvalido() {
        setContent()

        rule.onNodeWithTag("inputEmail").performTextInput("correo_malo")

        rule.onNodeWithTag("errorEmail").assertIsDisplayed()
    }

    @Test
    fun registerScreen_detectaContrasenasNoCoinciden() {
        setContent()

        rule.onNodeWithTag("inputPassword").performTextInput("123456")
        rule.onNodeWithTag("inputConfirmPassword").performTextInput("999999")

        rule.onNodeWithTag("errorPasswordMatch").assertIsDisplayed()
    }

    @Test
    fun registerScreen_botonDeshabilitadoCuandoElFormularioEsInvalido() {
        setContent()

        rule.onNodeWithTag("btnRegister").assertIsNotEnabled()
    }

    @Test
    fun registerScreen_botonHabilitadoCuandoFormularioEsValido() {
        setContent()

        rule.onNodeWithTag("inputUsername").performTextInput("Carlos")
        rule.onNodeWithTag("inputEmail").performTextInput("correo@correo.com")
        rule.onNodeWithTag("inputPassword").performTextInput("123456")
        rule.onNodeWithTag("inputConfirmPassword").performTextInput("123456")

        rule.onNodeWithTag("btnRegister").assertIsEnabled()
    }

    @Test
    fun registerScreen_disparaRegistroCuandoEsValido() {
        setContent()

        rule.onNodeWithTag("inputUsername").performTextInput("Carlos")
        rule.onNodeWithTag("inputEmail").performTextInput("correo@correo.com")
        rule.onNodeWithTag("inputPassword").performTextInput("123456")
        rule.onNodeWithTag("inputConfirmPassword").performTextInput("123456")

        rule.onNodeWithTag("btnRegister").performClick()

        assert(fakeVM.registerCalled)
        assert(navigated)
        assert(fakeVM.receivedEmail == "correo@correo.com")
    }

    @Test
    fun registerScreen_muestraErrorDeViewModel() {
        setContent()

        fakeVM.emitError("Error desde el servidor")

        rule.onNodeWithTag("errorViewModel").assertTextContains("Error desde el servidor")
    }
}
