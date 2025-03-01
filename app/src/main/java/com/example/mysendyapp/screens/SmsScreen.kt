package com.example.mysendyapp.screens

import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import land.sendy.pfe_sdk.api.API
import land.sendy.pfe_sdk.api.API.api
import land.sendy.pfe_sdk.model.pfe.response.AuthActivateRs
import land.sendy.pfe_sdk.model.pfe.response.BResponse
import land.sendy.pfe_sdk.model.types.ApiCallback
import land.sendy.pfe_sdk.model.types.LoaderError

@Preview(showBackground = true)
@Composable
fun SmsScreen(modifier: Modifier = Modifier) {

    var codeState by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = LocalActivity.current

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
                value = codeState,
                onValueChange = {
                    codeState = checkCode(it)
                },
                label = {
                    Text("Enter code:")
                },
                singleLine = true
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
                    if (codeState.length == 6) {
                        val phone = activity?.intent?.getStringExtra("phone")
                        if (phone != null) {
                            activate(phone, "sms", codeState, context)
                        }
                    }
                }
            ) {
                Text("Send code")
            }
        }
    }
}

fun checkCode(str: String): String {
    println(str)
    if (str.length <= 6) {
        if (str.isEmpty()) {
            return ""
        }
        if (str[0].digitToIntOrNull() == null) {
            return ""
        }
        for (i in 1..str.lastIndex) {
            if (str[i].digitToIntOrNull() == null) {
                return str.substring(0, i)
            }
        }
        return str
    }
    return str.substring(0, 6)
}

fun activate(myPhone: String, tokenType: String, token: String, context: Context) {

    API.outLog("Тест: WS. Попытка активации кошелька " + myPhone + ", " + tokenType + ": " + token)

    val runResult = api.activateWlletWS(context, token, tokenType, object : ApiCallback() {
        override fun <T : BResponse?> onSuccess(data: T) {
            if (data != null) {
                if (this.getErrNo() == 0) {
                    val response = this.oResponse as AuthActivateRs
                    if (response.TwoFactor != null && response.TwoFactor && API.checkString(response.Email)) {
                        API.outLog(
                            "Введите код активации из EMAIL " + response.Email
                        )
                    } else if (response.Active != null && response.Active) {
                        api.acivateDevice(context);
                        API.outLog("Девайс астивирован!")
                    }
                } else {
                    API.outLog("Сервер вернул ошибку; " + this.toString());
                }
            } else {
                API.outLog("onSuccess. Проблема: сервер не вернул данные!");
            }
        }

        override fun onFail(error: LoaderError) {
            API.outLog("Фатальная ошибка: " + error.toString());
        }
    })
    if (runResult != null && runResult.hasError()) {
        API.outLog("Запрос не был запущен:\r\n" + runResult.toString());
    }
}



