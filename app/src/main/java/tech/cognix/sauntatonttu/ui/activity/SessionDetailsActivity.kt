package tech.cognix.sauntatonttu.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.databinding.ActivitySessionDetailsBinding


class SessionDetailsActivity : AppCompatActivity() {

    private var _binding: ActivitySessionDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySessionDetailsBinding.inflate(layoutInflater)
        val view: View = _binding!!.root
        setContentView(view)

        _binding!!.sDate.text = "${intent.getStringExtra("s_date")}"
        _binding!!.sStartTime.text = "${intent.getStringExtra("s_start")}"
        _binding!!.sEndTime.text = "${intent.getStringExtra("s_end")}"
        _binding!!.sTotalDuration.text = "${intent.getStringExtra("s_duration")} Min"
        _binding!!.sAvgTemp.text = "${intent.getStringExtra("s_temp")} C"
        _binding!!.sAvgHumid.text = "${intent.getStringExtra("s_humid")} %"
        var pressure = (intent.getStringExtra("s_pressure")!!.toDouble() / 100).toString()
        _binding!!.sAvgPressure.text = "${pressure} Hpa"
        _binding!!.sAvgCalories.text = "${intent.getStringExtra("s_calories")} Calories"


    }
}