package com.example.miaumarket.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.data.remote.dto.LoginRequest
import com.example.miaumarket.domain.repository.AuthRepository
import com.example.miaumarket.ui.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState<String>>(AuthState.Idle)
    val authState: StateFlow<AuthState<String>> = _authState

    var username = MutableStateFlow("")
    var password = MutableStateFlow("")

    fun login() {
        val currentUsername = username.value
        val currentPassword = password.value

        if (currentUsername.isBlank() || currentPassword.isBlank()) {
            _authState.value = AuthState.Error("Please enter username and password")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.login(LoginRequest(currentUsername, currentPassword))
            result.onSuccess { token ->
                _authState.value = AuthState.Success(token)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }
}
