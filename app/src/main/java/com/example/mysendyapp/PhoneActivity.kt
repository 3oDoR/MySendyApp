package com.example.mysendyapp

import android.os.Bundle
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mysendyapp.screens.PhoneScreen
import com.example.mysendyapp.ui.theme.MySendyAppTheme
import land.sendy.pfe_sdk.activies.MasterActivity
import land.sendy.pfe_sdk.api.API

class PhoneActivity : MasterActivity() {

    private val SERVER_URL = "https://testwallet.sendy.land"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        api = API.getInsatce(SERVER_URL, "sendy")

        enableEdgeToEdge()
        setContent {
            MySendyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhoneScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}