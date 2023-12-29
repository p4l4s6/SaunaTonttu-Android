package tech.cognix.sauntatonttu.ui.activity.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.cognix.sauntatonttu.databinding.FragmentDashboardBinding
import tech.cognix.sauntatonttu.models.Dashboard
import tech.cognix.sauntatonttu.models.Device
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.activity.AddDeviceActivity
import tech.cognix.sauntatonttu.ui.activity.SessionDetailsActivity
import tech.cognix.sauntatonttu.ui.activity.SignupActivity
import tech.cognix.sauntatonttu.ui.adapters.DeviceAdapter
import tech.cognix.sauntatonttu.ui.adapters.SaunaSessionAdapter
import tech.cognix.sauntatonttu.ui.viewmodels.DeviceViewModel
import tech.cognix.sauntatonttu.utils.DateTimeUtils
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils

class DashboardFragment : Fragment(), SaunaSessionAdapter.OnAddBtnClickListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var progressBarUtils: ProgressBarUtils
    private var networkUtils: NetworkUtils = NetworkUtils()
    private var sessionList = mutableListOf<SaunaSession>()
    private lateinit var saunaSessionAdapter: SaunaSessionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = DashboardViewModel().initial(requireActivity())
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialize()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initialize() {
        progressBarUtils = ProgressBarUtils(requireContext(), _binding!!.progressBar)
        recyclerView = _binding!!.sessionRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        saunaSessionAdapter = SaunaSessionAdapter(requireContext(), sessionList, this)
        recyclerView.adapter = saunaSessionAdapter
        saunaSessionAdapter.notifyDataSetChanged()
        getDashboard()
        getSaunaSessions()

    }

    override fun onItemClick(saunaSession: SaunaSession) {
        val intent = Intent(requireActivity(), SessionDetailsActivity::class.java)
        intent.putExtra("s_date", DateTimeUtils().getDateFromServer(saunaSession.start_time))
        intent.putExtra("s_start", DateTimeUtils().getTimeFromServer(saunaSession.start_time))
        intent.putExtra("s_end", DateTimeUtils().getTimeFromServer(saunaSession.end_time))
        intent.putExtra("s_duration", saunaSession.duration)
        intent.putExtra("s_temp", saunaSession.avg_temp)
        intent.putExtra("s_humid", saunaSession.avg_humid)
        intent.putExtra("s_pressure", saunaSession.avg_pressure)
        intent.putExtra("s_calories", saunaSession.calories_burned)
        startActivity(intent)
    }

    private fun getSaunaSessions() {
        progressBarUtils.showProgressBar()
        dashboardViewModel.getSessions().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            sessionList = it.data.body()!!.data!!.results as ArrayList<SaunaSession>
                            saunaSessionAdapter.setData(sessionList)
                            saunaSessionAdapter.notifyDataSetChanged()
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
    }

    private fun getDashboard() {
        progressBarUtils.showProgressBar()
        dashboardViewModel.getDashboard().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            var dashboard = it.data.body()!!.data!!
                            var pressure = (dashboard.highest_pressure.toDouble() / 100).toString()
                            _binding!!.statTotalSession.text =  "${dashboard.total_session} Sessions"
                            _binding!!.statLongestSession.text = "${dashboard.longest_session} Min"
                            _binding!!.statHighestHumid.text = "${dashboard.highest_humidity} %"
                            _binding!!.statHighestAir.text = "${pressure} Hpa"
                            _binding!!.statHighestTemp.text = "${dashboard.highest_temp} C"
                            _binding!!.statHighestCalorie.text = "${dashboard.highest_calorie_burn} Calories"
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
    }
}