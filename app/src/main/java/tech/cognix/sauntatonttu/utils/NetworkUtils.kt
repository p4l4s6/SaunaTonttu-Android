package tech.cognix.sauntatonttu.utils

import org.json.JSONObject
import tech.cognix.sauntatonttu.response.BaseResponse

class NetworkUtils {

    fun isSuccess(statusCode:Int):Boolean{
        if (statusCode== Constants.HTTP_OK || statusCode==Constants.HTTP_CREATED) return true
        return false
    }
}