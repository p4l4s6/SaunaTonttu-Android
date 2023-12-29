package tech.cognix.sauntatonttu.ui.activity.ui.devices

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruuvi.station.bluetooth.IRuuviTagScanner
import com.ruuvi.station.bluetooth.RuuviRangeNotifier
import tech.cognix.sauntatonttu.databinding.FragmentDeviceBinding
import tech.cognix.sauntatonttu.models.Device
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.activity.AddDeviceActivity
import tech.cognix.sauntatonttu.ui.activity.HomeActivity
import tech.cognix.sauntatonttu.ui.adapters.DeviceAdapter
import tech.cognix.sauntatonttu.ui.viewmodels.AuthViewModel
import tech.cognix.sauntatonttu.ui.viewmodels.DeviceViewModel
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils
import tech.cognix.sauntatonttu.utils.SharedPreferencesApp

class DeviceFragment : Fragment(), DeviceAdapter.OnAddBtnClickListener {

    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var progressBarUtils: ProgressBarUtils
    private var networkUtils: NetworkUtils = NetworkUtils()
    private var devicesList = mutableListOf<Device>()
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deviceViewModel = DeviceViewModel().initial(requireActivity())
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialize()

        return root
    }

    private fun initialize() {
        progressBarUtils = ProgressBarUtils(requireContext(), _binding!!.progressBar)
        recyclerView = _binding!!.deviceRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        deviceAdapter = DeviceAdapter(requireContext(), devicesList, this)
        recyclerView.adapter = deviceAdapter
        deviceAdapter.notifyDataSetChanged()
        getDevices()

        _binding!!.addDeviceBtn.setOnClickListener{
            val intent = Intent(requireActivity(), AddDeviceActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(device: Device) {
        progressBarUtils.showProgressBar()
        deviceViewModel.deleteDevice(device.id!!).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            Toast.makeText(requireContext(), "Device Deleted", Toast.LENGTH_SHORT)
                                .show()
                            getDevices()
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

    private fun getDevices(){
        deviceViewModel.getDevices().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            devicesList = it.data.body()!!.data!!.results as ArrayList<Device>
                            deviceAdapter.setData(devicesList)
                            deviceAdapter.notifyDataSetChanged()
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