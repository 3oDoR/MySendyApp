package com.example.mysendyapp.activity

import android.os.Bundle
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mysendyapp.model.RegistrationRepository
import com.example.mysendyapp.screens.PhoneScreen
import com.example.mysendyapp.ui.theme.MySendyAppTheme
import com.example.mysendyapp.viewmodel.PhoneViewModel
import land.sendy.pfe_sdk.activies.MasterActivity
import land.sendy.pfe_sdk.api.API

private const val SERVER_URL = "https://testwallet.sendy.land"

class PhoneActivity : MasterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        api = API.getInsatce(SERVER_URL, "sendy")

        val lp = Looper.getMainLooper();

        enableEdgeToEdge()
        setContent {
            MySendyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhoneScreen(modifier = Modifier.padding(innerPadding), viewModel<PhoneViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return PhoneViewModel(RegistrationRepository()) as T
                            }
                        }
                    ))
                }
            }
        }
    }
}