package com.example.mysendyapp.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.mysendyapp.viewmodel.SmsViewModel
import kotlinx.coroutines.launch


@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    viewModel: SmsViewModel
) {

    val context = LocalContext.current
    val activity = LocalActivity.current
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(viewModel.smsCode))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldValue,
                onValueChange = {
                    if (viewModel.checkSmsCode(it.text)) {
                        textFieldValue = it
                    }

                },
                label = {
                    Text("Enter code:")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
        Text(
            text = viewModel.errorInfo
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (viewModel.smsCode.length == 6) {
                        val phone = activity?.intent?.getStringExtra("phone")
                        if (phone != null) {
                            viewModel.viewModelScope.launch {
                                try {
                                    viewModel.sendSmsCode(phone, "sms", viewModel.smsCode, context)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            ) {
                Text("Send code")
            }
        }
    }
}



