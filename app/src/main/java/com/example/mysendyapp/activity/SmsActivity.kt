package com.example.mysendyapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mysendyapp.model.RegistrationRepository
import com.example.mysendyapp.screens.SmsScreen
import com.example.mysendyapp.ui.theme.MySendyAppTheme
import com.example.mysendyapp.viewmodel.PhoneViewModel
import com.example.mysendyapp.viewmodel.SmsViewModel

class SmsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySendyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SmsScreen(modifier = Modifier.padding(innerPadding), viewModel<SmsViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return SmsViewModel(RegistrationRepository()) as T
                            }
                        }
                    ))
                }
            }
        }
    }
}