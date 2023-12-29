package tech.cognix.sauntatonttu.models

import java.io.Serializable

class SaunaSession : Serializable {
    constructor()

    var id: Int? = null
    var user: Int? = null
    var start_time: String? = null
    var end_time: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var duration: String? = null
    var avg_temp: String? = null
    var avg_humid: String? = null
    var avg_pressure: String? = null
    var calories_burned: String? = null
    var heartbit_increased: String? = null
    var blood_flow_increased: String? = null

    data class StartSession(
        var start_time: String
    )

    data class StopSession(
        var end_time: String
    )

    data class getSession(
        var id: Int? = null,
        var user: Int? = null,
        var start_time: String? = null,
        var end_time: String? = null,
        var created_at: String? = null,
        var updated_at: String? = null,
    )
}
    
