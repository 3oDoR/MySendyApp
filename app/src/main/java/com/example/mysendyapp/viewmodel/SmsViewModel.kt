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
        if (str.length <= 6) {
            if (str.isEmpty()) {
                smsCode = ""
                errorInfo = "Code less than 6 character"
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
            return
        }
        smsCode = str.substring(0, 6)
    }

    fun sendSmsCode(phone: String, tokenType: String, token: String, context: Context) {
        registrationRepository.activate(phone, tokenType, token, context)
    }

}