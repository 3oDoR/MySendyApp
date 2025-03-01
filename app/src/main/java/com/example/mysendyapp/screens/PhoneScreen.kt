package com.example.mysendyapp.screens

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession.Token
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mysendyapp.R
import com.example.mysendyapp.SmsActivity
import land.sendy.pfe_sdk.api.API
import land.sendy.pfe_sdk.api.API.api
import land.sendy.pfe_sdk.model.pfe.response.AuthActivateRs
import land.sendy.pfe_sdk.model.pfe.response.BResponse
import land.sendy.pfe_sdk.model.pfe.response.SettingsRs
import land.sendy.pfe_sdk.model.pfe.response.TermsOfUseRs
import land.sendy.pfe_sdk.model.types.ApiCallback
import land.sendy.pfe_sdk.model.types.LoaderError

@Preview(showBackground = true)
@Composable
fun PhoneScreen(modifier: Modifier = Modifier) {

    var strState by remember {
        mutableStateOf("")
    }
    var errorState by remember {
        mutableStateOf("")
    }


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
        Button(
            onClick = {
                getTermsOfUseWS(context)
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
                    if (strState.length == 12 && errorState.isEmpty()) {
                        if ( loginAtAuthWS(context, strState)) {
                            startNewActivity(context, SmsActivity::class.java)
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.btn_continue))
            }
        }
    }
}

private fun startNewActivity(context: Context, activityClass: Class<*>) {
    val intent = Intent(context, activityClass)
    context.startActivity(intent)
}

private fun checkTextField(str: String): Pair<String, String> {
    println(str)
    if (str.isEmpty()) {
        return Pair(str, "")
    }
    if (str.length == 13) {
        return Pair(str.dropLast(1), "")
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


fun getTermsOfUseWS(context: Context) {
    API.outLog("Тест: WS. Получение текста пользовательского соглашения мобильного приложения")
    val runResult = api.getTermsOfUseWS(context, object : ApiCallback() {

        override fun onCompleted(res: Boolean) {
            if (!res || getErrNo() != 0) {
                API.outLog("Тест: WS. Выполнение запроса завершилось с ошибкой:$context")

            } else {
                API.outLog("Тест: WS. Текст соглашения:\r\n" + (this.oResponse as TermsOfUseRs).TextTermsOfUse)
                API.outLog("Тест: WS. Текст 2: ${this.oResponse}")
            }
        }
    })
    if (runResult != null && runResult.hasError()) {
        API.outLog("Тест: WS. runResult ERROR: \r\n" + runResult.toString())
    } else {
        API.outLog("Тест: WS. getTermsOfUseWS: запущено асинхронно!")

    }
}

fun loginAtAuthWS(context: Context, phone: String): Boolean {
    API.outLog("[MyParceTag] Тест: WS. Попытка старта активации кошелька: $phone")
    val runResult = api.loginAtAuth(context, phone, object : ApiCallback() {
        override fun onCompleted(res: Boolean) {
            if (!res || errNo != 0) {
                API.outLog("[MyParceTag] Ошибка: $this")

            } else {
                val response = this.oResponse
                API.outLog("[MyParceTag] Тест: in else $response")
            }
        }
    })
    if (runResult != null && runResult.hasError()) {
        API.outLog(
            """
            runResult запрос не был запущен:
            $runResult
            """.trimIndent()
        )
        return false
    }
    return true
}

