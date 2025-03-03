package com.example.mysendyapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import com.example.mysendyapp.model.RegistrationRepository

class PhoneViewModel(private val registrationRepository: RegistrationRepository) : ViewModel() {

    var phone by mutableStateOf("")
        private set
    var errorInfo by mutableStateOf("")
        private set
    var isOffer by mutableStateOf(false)
        private set

    fun updateIsOffer() {
        isOffer = !isOffer
    }

    fun checkTextField(str: String) {
        if (str.isEmpty()) {
            phone = ""
            errorInfo = ""
            return
        }
        if (str[0] != '+') {
            phone = str.dropLast(1)
            errorInfo = "The first character should be a plus"
            return
        }
        if (str.length == 1) {
            phone = str
            errorInfo = ""
            return
        }
        if (str[1] != '7') {
            phone = str.dropLast(1)
            errorInfo = "The 1 character should be a 7"
            return
        }
        println(str)
        if (str.lastIndex > 11) {
            println("if")
            phone = str.substring(0, 12)
            errorInfo = "Max length"
            checkTextField(phone)
            return
        }
        for (i in 1..str.lastIndex) {
            if (str[i].digitToIntOrNull() == null) {
                phone = str.substring(0, i)
                errorInfo = "The $i character should be a number"
                return
            }
        }
        phone = str
        errorInfo = ""
    }

    suspend fun getTerms(context: Context) {
        if (checkInternetConnection(context)) {
            errorInfo = registrationRepository.getTermsOfUseWS(context).parseAsHtml().toString()
        } else {
            errorInfo = "Don't have Internet"
        }
    }

    fun sendNumber(context: Context): Boolean {
        if (phone.length != 12) {
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
        val auth = registrationRepository.loginAtAuthWS(context, phone)
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