package com.example.miaumarket.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.data.remote.dto.RegisterRequest
import com.example.miaumarket.data.remote.dto.UserResponse
import com.example.miaumarket.domain.repository.AuthRepository
import com.example.miaumarket.ui.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState<UserResponse>>(AuthState.Idle)
    val authState: StateFlow<AuthState<UserResponse>> = _authState

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

        if (fName.isBlank() || lName.isBlank() || bDate.isBlank() || mail.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(
                RegisterRequest(
                    firstName = fName,
                    lastName = lName,
                    birthDate = bDate,
                    email = mail,
                    password = pass
                )
            )
            result.onSuccess { user ->
                _authState.value = AuthState.Success(user)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Registration failed")
            }
        }
    }
}
