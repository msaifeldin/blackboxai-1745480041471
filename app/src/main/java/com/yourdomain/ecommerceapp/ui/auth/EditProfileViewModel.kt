package com.yourdomain.ecommerceapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.yourdomain.ecommerceapp.data.model.User
import com.yourdomain.ecommerceapp.ui.common.BaseViewModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class EditProfileViewModel : BaseViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _isProfileUpdated = MutableLiveData<Boolean>()
    val isProfileUpdated: LiveData<Boolean> = _isProfileUpdated

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
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

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    currentUser.updateProfile(profileUpdates).await()
                    currentUser.updateEmail(email).await()

                    _isProfileUpdated.postValue(true)
                    clearError()
                } else {
                    setError("User not logged in")
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}
