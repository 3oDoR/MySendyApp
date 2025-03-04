package com.example.mysendyapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mysendyapp.model.RegistrationRepository

class SmsViewModel(private val registrationRepository: RegistrationRepository) : ViewModel() {

    var smsCode by mutableStateOf("")
        private set
    var errorInfo by mutableStateOf("")
        private set

    fun checkSmsCode(str: String) {
        if (str.isEmpty()) {
            smsCode = ""
            errorInfo = ""
            return
        }
        if (str.length > 6) {
            println("here 1 $smsCode and ${smsCode.length}")
            smsCode = str.substring(0, 6)
            println("here 2 $smsCode")
            errorInfo = "Max length"
            checkSmsCode(smsCode)
            return
        }

        for (i in 0..str.lastIndex) {
            if (str[i].digitToIntOrNull() == null) {
                smsCode = str.substring(0, i)
                errorInfo = "The $i character should be a number"
                return
            }
        }
        smsCode = str
        errorInfo = ""
    }

    suspend fun sendSmsCode(phone: String, tokenType: String, token: String, context: Context) {
        if (checkInternetConnection(context)) {
            errorInfo = ""
            val res = registrationRepository.activate(phone, tokenType, token, context)
            errorInfo = res
        } else {
            errorInfo = "Don't have Internet"
        }
    }

    private fun checkInternetConnection(context: Context): Boolean {
        return registrationRepository.isInternetAvailable(context)

    }

}