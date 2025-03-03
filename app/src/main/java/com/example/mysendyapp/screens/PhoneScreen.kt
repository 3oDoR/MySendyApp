package com.example.mysendyapp.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mysendyapp.R
import com.example.mysendyapp.activity.SmsActivity
import com.example.mysendyapp.viewmodel.PhoneViewModel


@Composable
fun PhoneScreen(
    modifier: Modifier = Modifier,
    viewModel: PhoneViewModel,
) {


    val context = LocalContext.current

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
            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.phone,
                    onValueChange = {
                        viewModel.checkTextField(it)
                    },
                    label = {
                        Text("Enter phone number:")
                    },
                    placeholder = {
                        Text("+7 XXX XXX XX XX")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
                Text(text = viewModel.errorInfo)
            }
        }
        Row {
            Button(
                onClick = {
                    viewModel.getTerms(context)
                }
            ) {
                Text("getTerms")
            }
            Checkbox(
                checked = viewModel.isOffer,
                onCheckedChange = { viewModel.updateIsOffer() }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                        if (viewModel.sendNumber(context)) {
                            val intent = Intent(context, SmsActivity::class.java).apply {
                                putExtra("phone", viewModel.phone)
                            }
                            context.startActivity(intent)
                        }
                }
            ) {
                Text(stringResource(R.string.btn_continue))
            }
        }
    }
}
