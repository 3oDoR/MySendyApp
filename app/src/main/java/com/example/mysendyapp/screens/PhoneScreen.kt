package com.example.mysendyapp.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mysendyapp.R

@Preview(showBackground = true)
@Composable
fun PhoneScreen() {

    var strState by remember {
        mutableStateOf("")
    }
    var errorState by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
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
                    value = strState,
                    onValueChange = {
                        val res = checkTextField(it)
                        strState = res.first
                        errorState = res.second
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
                Text(text = errorState)
            }
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

                }
            ) {
                Text(stringResource(R.string.btn_continue))
            }
        }
    }
}

fun checkTextField(str: String): Pair<String, String> {
    println(str)
    if (str.isEmpty()) {
        return Pair(str, "")
    }
    if (str.length == 13) {
         return Pair(str.dropLast(1), "Number can't be longer")
    }
    if (str[0] != '+') {
        return Pair(str.dropLast(1), "The first character should be a plus")
    }
    if (str.length == 1) {
        return Pair(str, "")
    }
    if (str[1] != '7') {
        return Pair(str.dropLast(1), "The 1 character should be a 7")
    }
    for (i in 2..str.lastIndex) {
        if (str[i].digitToIntOrNull() == null) {
            return Pair(str.substring(0, i), "The $i character should be a number")
        }
    }
    return Pair(str, "")
}