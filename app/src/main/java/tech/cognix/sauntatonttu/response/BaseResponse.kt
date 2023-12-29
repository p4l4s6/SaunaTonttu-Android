package tech.cognix.sauntatonttu.response

import org.json.JSONObject

data class BaseResponse<T>(
    var code: Int,
    var data: T? = null,
    var message: String?=null,
    var status: String?,
    var errors: JSONObject?=null,
)