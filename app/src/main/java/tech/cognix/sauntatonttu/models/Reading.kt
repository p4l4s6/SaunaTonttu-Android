package tech.cognix.sauntatonttu.models

import java.io.Serializable

class Reading : Serializable {
    constructor()

    var id: Int? = null
    var user: Int? = null
    var device: String? = null
    var session: Int? = null
    var pressure: String? = null
    var temp: String? = null
    var humidity: String? = null
    var timestamp: String? = null
    var created_at: String? = null
    var updated_at: String? = null


    data class AddData(
        var session: Int,
        var device: String,
        var pressure: String,
        var temp: String,
        var humidity: String,
        var timestamp: String,
    )

    data class ReadData(
        var id: Int? = null,
        var user: Int? = null,
        var session: Int? = null,
        var pressure: String? = null,
        var temp: String? = null,
        var humidity: String? = null,
        var timestamp: String? = null,
        var created_at: String? = null,
        var updated_at: String? = null,
    )


}