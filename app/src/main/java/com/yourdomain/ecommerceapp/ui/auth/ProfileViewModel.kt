package com.yourdomain.ecommerceapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yourdomain.ecommerceapp.data.model.User
import com.yourdomain.ecommerceapp.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // For simplicity, create User object from FirebaseUser
                    val userData = User(
                        id = currentUser.uid,
                        name = currentUser.displayName ?: "",
                        email = currentUser.email ?: ""
                    )
                    _user.postValue(userData)
                    clearError()
                } else {
                    setError("User not logged in")
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun retryLoading() {
        loadUser()
    }
}
