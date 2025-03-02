package com.example.mysendyapp.model

import android.content.Context
import android.os.Looper
import land.sendy.pfe_sdk.api.API
import land.sendy.pfe_sdk.api.API.api
import land.sendy.pfe_sdk.model.pfe.response.AuthActivateRs
import land.sendy.pfe_sdk.model.pfe.response.BResponse
import land.sendy.pfe_sdk.model.pfe.response.TermsOfUseRs
import land.sendy.pfe_sdk.model.types.ApiCallback
import land.sendy.pfe_sdk.model.types.LoaderError

class RegistrationRepository {


    fun getTermsOfUseWS(context: Context) {

        API.outLog("Получение текста пользовательского соглашения мобильного приложения")
        val runResult = api.getTermsOfUse(context, object : ApiCallback() {
            override fun onCompleted(res: Boolean) {
                if (!res || errNo != 0) {
                    API.outLog("Выполнение запроса завершилось с ошибкой:${oResponse}")

                } else {
                    API.outLog("Текст соглашения:\r\n ${(oResponse as TermsOfUseRs).TextTermsOfUse}")

                }
            }
        })
        if (runResult != null && runResult.hasError()) {
            API.outLog("runResult ERROR: \r\n$runResult")
        } else {
            API.outLog("getTermsOfUseWS: запущено асинхронно!")
        }
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

    fun activate(myPhone: String, tokenType: String, token: String, context: Context) {

        API.outLog("Тест: WS. Попытка активации кошелька " + myPhone + ", " + tokenType + ": " + token)
        val runResult = api.activateWllet(context, token, tokenType, object : ApiCallback() {
            override fun <T : BResponse?> onSuccess(data: T) {
                API.outLog("m data $data")
                API.outLog("m Errno ${oResponse.Errno}")
                API.outLog("m Error  ${oResponse.Error}")
                API.outLog("m Reply  ${oResponse.Reply}")
                API.outLog("m Subject  ${oResponse.Subject}")
                if (data != null) {
                    if (this.errNo == 0) {
                        val response = this.oResponse as AuthActivateRs
                        // {"Errno":0,"Reply":"pfe/auth/activate","Subject":"82a0afb9-7ba3-4a5b-bbba-815105cbcac6"}
                        API.outLog("$response")
                        // Ставлю вручную, так как не понял, как получить другой ответ от сервера (приходит ответ с ошибкой errno или ответ, как указано выше).
                        response.Active = true
                        if (response.TwoFactor != null && response.TwoFactor && API.checkString(response.Email)) {
                            API.outLog("Введите код активации из EMAIL " + response.Email)
                        } else if (response.Active != null && response.Active) {
                            API.outLog("Девайс астивирован!")
                            api.acivateDevice(context)
                        } else {

                        }
                    } else {
                        API.outLog("Сервер вернул ошибку; " + this.toString());
                    }
                } else {
                    API.outLog("onSuccess. Проблема: сервер не вернул данные!");
                }
                API.outLog("IsActivated ${api.isActivated(context)}")
            }

            override fun onFail(error: LoaderError) {
                API.outLog("Фатальная ошибка: " + error.toString());
            }
        })
        if (runResult != null && runResult.hasError()) {
            API.outLog("Запрос не был запущен:\r\n" + runResult.toString());
        }
    }
}