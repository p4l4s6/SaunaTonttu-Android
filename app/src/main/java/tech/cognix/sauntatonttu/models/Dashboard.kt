package tech.cognix.sauntatonttu.models

data class Dashboard(
    val highest_calorie_burn: String,
    val highest_humidity: String,
    val highest_pressure: String,
    val highest_temp: String,
    val longest_session: String,
    val total_session: String
)