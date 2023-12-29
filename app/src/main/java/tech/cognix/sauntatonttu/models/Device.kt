package tech.cognix.sauntatonttu.models

import java.io.Serializable

class Device : Serializable {
    constructor()

    
    var id: Int? = null
    var is_active: Boolean? = null
    var mac_address: String? = null
    var model: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var user: Int? = null


    data class GetDevice(
        var id: Int?,
        var is_active: Boolean?,
        var mac_address: String?,
        var model: String?,
        var created_at: String?,
        var updated_at: String?,
        var user: Int?
    )

    data class AddDevice(
        var model: String?,
        var mac_address: String?,
        var is_active: Boolean?
    )
}