package com.example.mysendyapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysendyapp.model.RegistrationRepository
import kotlinx.coroutines.launch

class PhoneViewModel(private val registrationRepository: RegistrationRepository) : ViewModel() {

    var phone by mutableStateOf("")
        private set
    var errorInfo by mutableStateOf("")
        private set
    var isOffer by mutableStateOf(false)
        private set
    var openDialog by mutableStateOf(false)
        private set
    var userAgreements by mutableStateOf("D")

    fun updateIsOffer() {
        isOffer = !isOffer
    }

    fun acceptOffer() {
        isOffer = true
    }

    fun dismissOffer() {
        isOffer = false
    }

    fun clearErrorMessage() {
        errorInfo = ""
    }

    fun openDialog() {
        openDialog = !openDialog
    }

    fun checkTextField(str: String): Boolean {
        if (str.length > 10) {
            return false
        }
        if (str.isEmpty()) {
            phone = ""
            errorInfo = ""
            return true
        }

        for (i in 0..str.lastIndex) {
            if (str[i].digitToIntOrNull() == null) {
                phone = str.substring(0, i)
                errorInfo = "The ${i + 1} character should be a number"
                return false
            }
        }
        phone = str
        errorInfo = ""
        return true
    }

    suspend fun getTerms(context: Context) {
        if (checkInternetConnection(context)) {
            userAgreements = registrationRepository.getTermsOfUseWS(context).parseAsHtml().toString()
        } else {
            userAgreements = "Don't have Internet"
        }
    }

fun sendNumber(context: Context): Boolean {
    val fullNumber = "+7${phone}"
    if (fullNumber.length != 12) {
        errorInfo = "Enter full number"
        return false
    }
    if (!isOffer) {
        errorInfo = "Offer is not accept"
        return false
    }
    if (!checkInternetConnection(context)) {
        errorInfo = "Don't have Internet"
        return false
    }
    val auth = registrationRepository.loginAtAuthWS(context, fullNumber)
    if (auth != null) {
        if (auth.errorText.isNotEmpty()) {
            errorInfo = auth.errorText
            return false
        }
    }
    errorInfo = ""
    return true
}

private fun checkInternetConnection(context: Context): Boolean {
    return registrationRepository.isInternetAvailable(context)

}
}