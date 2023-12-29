package tech.cognix.sauntatonttu.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ruuvi.station.bluetooth.FoundRuuviTag
import com.ruuvi.station.bluetooth.IRuuviTagScanner
import com.ruuvi.station.bluetooth.RuuviRangeNotifier
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.Device
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.adapters.RuuviTagAdapter
import tech.cognix.sauntatonttu.ui.viewmodels.DeviceViewModel
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils

class AddDeviceActivity : AppCompatActivity(), IRuuviTagScanner.OnTagFoundListener, RuuviTagAdapter.OnAddBtnClickListener {

    private lateinit var ruuviRangeNotifier: IRuuviTagScanner
    private var ruuviTagList = mutableListOf<FoundRuuviTag>()
    private var knownMacList = mutableListOf<String>()
    private var foundIDs = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RuuviTagAdapter
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var progressBarUtils: ProgressBarUtils
    private var networkUtils: NetworkUtils = NetworkUtils()
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        progressBar = findViewById(R.id.progressBar)
        progressBarUtils = ProgressBarUtils(this, progressBar)
        deviceViewModel = DeviceViewModel().initial(this)

        recyclerView = findViewById(R.id.ruuvi_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = RuuviTagAdapter(this, ruuviTagList, knownMacList, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        progressBarUtils.showProgressBar()

        deviceViewModel.getDevices().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            for (item in it.data.body()!!.data!!.results) {
                                knownMacList.add(item.mac_address!!)
                            }
                            startScanning()
                        }

                    }

                    Status.ERROR -> {
                        progressBarUtils.hideProgressBar()
                        Toast.makeText(this, "Error! try again", Toast.LENGTH_SHORT).show()
                    }

                    Status.LOADING -> {
                        progressBarUtils.showProgressBar()
                    }
                }
            }
        }

        ruuviRangeNotifier = RuuviRangeNotifier(application, "AddDeviceActivity")

    }

    override fun onResume() {
        super.onResume()
        startScanning()
    }

    override fun onPause() {
        super.onPause()
        ruuviRangeNotifier.stopScanning()
    }

    fun startScanning() {
        ruuviRangeNotifier.startScanning(this)
    }

    override fun onTagFound(tag: FoundRuuviTag) {
        if (!foundIDs.contains(tag.id.toString())) {
            foundIDs.add(tag.id.toString())
            ruuviTagList.add(tag)
            adapter.setData(ruuviTagList, knownMacList)
            adapter.notifyDataSetChanged()
        }

    }

    override fun onItemClick(tag: FoundRuuviTag) {
        var device =Device()
        device.mac_address=tag.id.toString()
        device.model= tag.id.toString()
        progressBarUtils.showProgressBar()
        deviceViewModel.addDevice(device).observe(this){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            Toast.makeText(this, "Device added successfully", Toast.LENGTH_SHORT).show()
                        }

                    }

                    Status.ERROR -> {
                        progressBarUtils.hideProgressBar()
                        Toast.makeText(this, "Error! try again", Toast.LENGTH_SHORT).show()
                    }

                    Status.LOADING -> {
                        progressBarUtils.showProgressBar()
                    }
                }
            }
        }
    }
}