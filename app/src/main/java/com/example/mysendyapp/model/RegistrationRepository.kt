package com.example.mysendyapp.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import land.sendy.pfe_sdk.api.API
import land.sendy.pfe_sdk.api.API.api
import land.sendy.pfe_sdk.model.pfe.response.AuthActivateRs
import land.sendy.pfe_sdk.model.pfe.response.BResponse
import land.sendy.pfe_sdk.model.pfe.response.TermsOfUseRs
import land.sendy.pfe_sdk.model.types.ApiCallback
import land.sendy.pfe_sdk.model.types.LoaderError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RegistrationRepository {

    suspend fun getTermsOfUseWS(context: Context): String {
        API.outLog("Получение текста пользовательского соглашения мобильного приложения")

        try {
            val termsOfUseRs = suspendCancellableCoroutine { continuation ->
                api.getTermsOfUse(context, object : ApiCallback() {
                    override fun onCompleted(res: Boolean) {
                        if (!res || errNo != 0) {
                            continuation.resumeWithException(Exception("Выполнение запроса завершилось с ошибкой: ${oResponse}"))
                        } else {
                            continuation.resume(oResponse as TermsOfUseRs)
                        }
                    }
                })
            }
            API.outLog("Текст соглашения:\r\n ${termsOfUseRs.TextTermsOfUse}")
            return termsOfUseRs.TextTermsOfUse.toString()
        } catch (e: Exception) {
            API.outLog("Ошибка при получении текста соглашения: ${e.message}")
        }
        return "Err"
    }

    fun loginAtAuthWS(context: Context, phone: String): LoaderError? {
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
        }
        return runResult
    }

    suspend fun activate(myPhone: String, tokenType: String, token: String, context: Context): String {
        API.outLog("Тест: WS. Попытка активации кошелька " + myPhone + ", " + tokenType + ": " + token)
        try {
            val act = suspendCancellableCoroutine { continuation ->
                api.activateWllet(context, token, tokenType, object : ApiCallback() {
                    override fun <T : BResponse?> onSuccess(data: T) {
                        if (data != null) {
                            if (this.errNo == 0) {
                                val response = this.oResponse as AuthActivateRs
                                response.Active = true
                                if (response.TwoFactor != null && response.TwoFactor && API.checkString(
                                        response.Email
                                    )
                                ) {
                                    API.outLog("Введите код активации из EMAIL " + response.Email)
                                    continuation.resume(oResponse.Errno)
                                } else if (response.Active != null && response.Active) {
                                    API.outLog("Девайс астивирован!")
                                    api.acivateDevice(context)
                                    continuation.resume(oResponse.Errno)
                                }
                            } else {
                                API.outLog("Сервер вернул ошибку; " + this.toString());
                                continuation.resume(oResponse.Errno)
                            }
                        } else {
                            API.outLog("onSuccess. Проблема: сервер не вернул данные!");
                            continuation.resume(oResponse.Errno)
                        }
                        API.outLog("IsActivated ${api.isActivated(context)}")
                    }
                })
            }
            if (act.toString() == "0") {
                return "Successs"
            }  else {
                return "Fail"
            }
        } catch (e: Exception) {
            API.outLog("Error")
        }
        return "False"
    }
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities != null && (
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
        }
    }