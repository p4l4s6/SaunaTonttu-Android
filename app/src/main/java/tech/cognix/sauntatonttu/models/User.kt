package tech.cognix.sauntatonttu.models

import java.io.Serializable

class User : Serializable {
    constructor()

    var id: Int? = null
    var first_name: String? = null
    var last_name: String? = null
    var email: String? = null
    var image: String? = null
    var gender: Int? = null
    var is_verified: Boolean? = null
    var is_active: Boolean? = null
    var token: String? = null
    var password: String? = null
    var image_url: String? = null
    var confirm_password: String? = null

    data class Login(
        var email: String? = null,
        var password: String? = null
    )

    data class ForgotPass(
        var email: String? = null
    )

    data class ForgotPassConfirm(
        var email: String? = null,
        var code: String? = null,
        var password: String? = null,
    )

    data class ChangePassword(
        var old_password: String? = null,
        var password: String? = null,
        var confirm_password: String? = null
    )


    data class SignUp(
        var first_name: String? = null,
        var last_name: String? = null,
        var email: String? = null,
        var password: String? = null,
        var confirm_password: String? = null
    )

}
