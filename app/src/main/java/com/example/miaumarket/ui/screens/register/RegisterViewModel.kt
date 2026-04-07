package com.example.miaumarket.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.data.remote.dto.RegisterRequest
import com.example.miaumarket.domain.repository.AuthRepository
import com.example.miaumarket.ui.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState<String>>(AuthState.Idle)
    val authState: StateFlow<AuthState<String>> = _authState

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var birthDate = MutableStateFlow("")
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")

    fun register() {
        val fName = firstName.value
        val lName = lastName.value
        val bDate = birthDate.value
        val mail = email.value
        val pass = password.value
        val isoBirthDate = bDate.toIsoBirthDate()

        if (fName.isBlank() || lName.isBlank() || bDate.isBlank() || mail.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Rellena todos los campos")
            return
        }

        if (isoBirthDate == null) {
            _authState.value = AuthState.Error("Selecciona una fecha de nacimiento válida")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(
                RegisterRequest(
                    firstName = fName,
                    lastName = lName,
                    birthDate = isoBirthDate,
                    email = mail,
                    password = pass
                )
            )
            result.onSuccess { token ->
                _authState.value = AuthState.Success(token)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "No se ha podido completar el registro")
            }
        }
    }
}

private val displayBirthDateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")

private fun String.toIsoBirthDate(): String? {
    return try {
        LocalDate.parse(trim(), displayBirthDateFormatter).format(DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (_: DateTimeParseException) {
        null
    }
}

