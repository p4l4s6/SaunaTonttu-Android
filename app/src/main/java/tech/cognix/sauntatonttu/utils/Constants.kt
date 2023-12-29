package tech.cognix.sauntatonttu.utils

interface Constants {
    companion object {
        val DateFormatServer: String = "yyyy-MM-dd'T'HH:mm:ss"
        const val BASE_URL = "https://saunatonttu.cognix.tech/api/"
        const val CONTENT_LANGUAGE = "Content-Language"
        const val LANG = "language"
        const val AUTHORIZATION = "Authorization"
        const val TOKEN = "token"
        const val INTRO = "INTRO"
        const val EMAIL = "email"
        const val USER = "user"
        const val HTTP_OK = 200
        const val HTTP_CREATED = 201
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_NO_CONTENT = 204
        const val HTTP_NOT_FOUND = 404
        const val TYPE_SUBMIT = 0
        const val TYPE_ADD_IMAGE = 1
        const val CURRENT_SESSION = "current_session"
        const val CURRENT_SESSION_START = "current_session_start"

    }
}
