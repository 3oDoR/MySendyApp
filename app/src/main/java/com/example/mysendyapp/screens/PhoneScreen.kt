package com.example.mysendyapp.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mysendyapp.R
import com.example.mysendyapp.activity.SmsActivity
import com.example.mysendyapp.viewmodel.PhoneViewModel
import kotlinx.coroutines.launch


@Composable
fun PhoneScreen(
    modifier: Modifier = Modifier,
    viewModel: PhoneViewModel,
) {


    val context = LocalContext.current

    MyAlertDialog(viewModel)
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
                Text("Enter phone number:")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.phone,
                    onValueChange = {
                        viewModel.checkTextField(it)
                    },
                    leadingIcon = {
                        Text("+7")
                    },
                    placeholder = {
                        Text("XXX XXX XX XX")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
                LazyColumn {
                    items(1) {
                        Text(text = viewModel.errorInfo)
                    }
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.viewModelScope.launch {
                    try {
                        viewModel.getTerms(context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    viewModel.openDialog()
                }
            }
        ) {
            Text("getTerms")
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


@Composable
fun MyAlertDialog(viewModel: PhoneViewModel = viewModel()) {
    if (viewModel.openDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.openDialog() },
            title = { Text(text = "User agreement") },
            text = {
                LazyColumn {
                    items(1) {
                        Text(viewModel.userAgreements)
                    }
                }
            },
            confirmButton = {
                Button({
                    viewModel.openDialog()
                    viewModel.acceptOffer()
                    viewModel.clearErrorMessage()
                }) {
                    Text("Accept", fontSize = 22.sp)
                }
            },
            dismissButton = {
                Button({
                    viewModel.openDialog()
                    viewModel.dismissOffer()
                    viewModel.clearErrorMessage()
                }) {
                    Text("Cancel", fontSize = 22.sp)
                }
            }
        )
    }
}
