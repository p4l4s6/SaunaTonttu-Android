package tech.cognix.sauntatonttu.ui.activity.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ruuvi.station.bluetooth.FoundRuuviTag
import com.ruuvi.station.bluetooth.IRuuviTagScanner
import com.ruuvi.station.bluetooth.RuuviRangeNotifier
import tech.cognix.sauntatonttu.databinding.FragmentHomeBinding
import tech.cognix.sauntatonttu.models.Reading
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.viewmodels.DeviceViewModel
import tech.cognix.sauntatonttu.utils.Constants
import tech.cognix.sauntatonttu.utils.DateTimeUtils
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils
import tech.cognix.sauntatonttu.utils.SharedPreferencesApp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment(), IRuuviTagScanner.OnTagFoundListener {
    private lateinit var ruuviRangeNotifier: IRuuviTagScanner
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var circularProgressBar: CircularProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var isStarted = false
    private lateinit var timeFormat: SimpleDateFormat
    private var startTime = 0L
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var progressBarUtils: ProgressBarUtils
    private var networkUtils: NetworkUtils = NetworkUtils()
    private lateinit var sharedPreferencesApp: SharedPreferencesApp
    private lateinit var saunaSession: SaunaSession
    private var sessionId = 0
    private var knownMacList = mutableListOf<String>()
    private var hasDevice = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = HomeViewModel().initial(requireActivity())
        deviceViewModel = DeviceViewModel().initial(requireActivity())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ruuviRangeNotifier = RuuviRangeNotifier(requireContext(), "home fragment")
        sharedPreferencesApp = SharedPreferencesApp(requireContext())
        progressBarUtils = ProgressBarUtils(requireContext(), _binding!!.progressBar)

        circularProgressBar = _binding!!.circularProgressBar
        circularProgressBar.progressMax = 3600f
        circularProgressBar.progress = 3600f
        timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        timeFormat.timeZone = TimeZone.getTimeZone("UTC")

        saunaSession = SaunaSession()
        sessionId = sharedPreferencesApp.getNumber(Constants.CURRENT_SESSION, 0)
        progressBarUtils.showProgressBar()
        deviceViewModel.getDevices().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            for (item in it.data.body()!!.data!!.results) {
                                knownMacList.add(item.mac_address!!)
                            }
                            if (knownMacList.size > 0) {
                                hasDevice = true
                            }
                            if (sessionId != 0) {
                                saunaSession.id = sessionId
                                startTime =
                                    sharedPreferencesApp.getText(Constants.CURRENT_SESSION_START)!!
                                        .toLong()
                                isStarted = true
                                _binding!!.actionBtn.text = "Stop"
                                startTimer()
                                startScanning()
                            }
                        }

                    }

                    Status.ERROR -> {
                        progressBarUtils.hideProgressBar()
                        Toast.makeText(requireContext(), "Error! try again", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        progressBarUtils.showProgressBar()
                    }
                }
            }
        }
        _binding!!.actionBtn.setOnClickListener {
            if (isStarted) {
                saunaSession.end_time = DateTimeUtils().getFormattedTime(System.currentTimeMillis())
                progressBarUtils.showProgressBar()
                homeViewModel.stopSession(saunaSession).observe(requireActivity()) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                progressBarUtils.hideProgressBar()
                                if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                                    isStarted = false
                                    _binding!!.actionBtn.text = "Start"
                                    _binding!!.timeText.text = "00:00:00"
                                    handler.removeCallbacks(runnable)
                                    sharedPreferencesApp.saveNumber(Constants.CURRENT_SESSION, 0)
                                    sharedPreferencesApp.saveText(
                                        Constants.CURRENT_SESSION_START,
                                        ""
                                    )
                                    ruuviRangeNotifier.stopScanning()
                                }

                            }

                            Status.ERROR -> {
                                progressBarUtils.hideProgressBar()
                                Toast.makeText(
                                    requireContext(),
                                    "Error! try again",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            Status.LOADING -> {
                                progressBarUtils.showProgressBar()
                            }
                        }
                    }
                }
                _binding!!.temp.text="Temperature: N/A"
                _binding!!.humidity.text="Humidity: N/A"
                _binding!!.pressure.text="Pressure: N/A"
            } else {
                if (hasDevice) {
                    progressBarUtils.showProgressBar()
                    startTime = System.currentTimeMillis()
                    saunaSession.start_time = DateTimeUtils().getFormattedTime(startTime)
                    homeViewModel.startSession(saunaSession).observe(requireActivity()) {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    progressBarUtils.hideProgressBar()
                                    if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                                        isStarted = true
                                        _binding!!.actionBtn.text = "Stop"
                                        startTimer()
                                        saunaSession.id = it.data.body()!!.data!!.id
                                        sessionId = saunaSession.id!!
                                        sharedPreferencesApp.saveNumber(
                                            Constants.CURRENT_SESSION,
                                            saunaSession.id!!
                                        )
                                        sharedPreferencesApp.saveText(
                                            Constants.CURRENT_SESSION_START,
                                            startTime.toString()
                                        )
                                        startScanning()
                                    }

                                }

                                Status.ERROR -> {
                                    progressBarUtils.hideProgressBar()
                                    Toast.makeText(
                                        requireContext(),
                                        "Error! try again",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                                Status.LOADING -> {
                                    progressBarUtils.showProgressBar()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please add a device first", Toast.LENGTH_LONG)
                        .show()
                }

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isStarted){
            handler.removeCallbacks(runnable)
            ruuviRangeNotifier.stopScanning()
        }
        _binding = null
    }

    private fun startTimer() {
        runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val formattedTime = timeFormat.format(Date(currentTime - startTime))
                _binding!!.timeText.text = formattedTime
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    fun startScanning() {
        ruuviRangeNotifier.startScanning(this)
    }

    override fun onTagFound(tag: FoundRuuviTag) {
        if (knownMacList.contains(tag.id.toString())) {
            var temp = tag.temperature.toString()
            var humid = tag.humidity.toString()
            var pressure = tag.pressure.toString()
            _binding!!.temp.text = "Temperature: $temp"
            _binding!!.humidity.text = "Humidity: $humid"
            _binding!!.pressure.text = "Pressure: $pressure"
            var reading = Reading()
            reading.session = sessionId
            reading.temp = temp
            reading.humidity = humid
            reading.pressure = pressure
            reading.device = tag.id
            reading.timestamp = DateTimeUtils().getFormattedTime(System.currentTimeMillis())
            progressBarUtils.showProgressBar()
            homeViewModel.addData(reading).observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBarUtils.hideProgressBar()
                            if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                                //
                            }

                        }

                        Status.ERROR -> {
                            progressBarUtils.hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "Error! try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        Status.LOADING -> {
                            progressBarUtils.showProgressBar()
                        }
                    }
                }
            }
        }

    }

}
