package tech.cognix.sauntatonttu.ui.adapters

import android.content.Context
import android.health.connect.datatypes.units.Length
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ruuvi.station.bluetooth.FoundRuuviTag
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.Device

class RuuviTagAdapter(
    private val context: Context,
    private var devices: MutableList<FoundRuuviTag>,
    private var knownList: MutableList<String>,
    private val listener: OnAddBtnClickListener
) :
    RecyclerView.Adapter<RuuviTagAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceNameTextView: TextView = itemView.findViewById(R.id.device_name)
        val deviceMacTextView: TextView = itemView.findViewById(R.id.device_mac)
        val deviceAddBtn: Button = itemView.findViewById(R.id.device_add_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_ruuvitag, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]

        holder.deviceNameTextView.text = device.id
        holder.deviceMacTextView.text = device.id
        if (knownList.contains(device.id.toString())) {
            holder.deviceAddBtn.text = "Added"
        }
        holder.deviceAddBtn.setOnClickListener {
            if (knownList.contains(device.id)) {
                Toast.makeText(context, "Already added", Toast.LENGTH_SHORT).show()
            } else {
                listener.onItemClick(device)
            }
        }

    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun setData(devices: MutableList<FoundRuuviTag>, knownList: MutableList<String>) {
        this.devices = devices
        this.knownList = knownList
    }

    interface OnAddBtnClickListener {
        fun onItemClick(tag: FoundRuuviTag)
    }
}
