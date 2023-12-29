package tech.cognix.sauntatonttu.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.Device

class DeviceAdapter(
    private val context: Context,
    private var devices: MutableList<Device>,
    private val listener: OnAddBtnClickListener
) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceNameTextView: TextView = itemView.findViewById(R.id.device_name)
        val deviceMacTextView: TextView = itemView.findViewById(R.id.device_mac)
        val deviceStatusTextView: TextView = itemView.findViewById(R.id.device_status)
        val deviceDeleteBtn: ImageView = itemView.findViewById(R.id.device_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]

        holder.deviceNameTextView.text = device.model
        holder.deviceMacTextView.text = device.mac_address
        if (device.is_active!!) {
            holder.deviceStatusTextView.text = "Active"
        } else {
            holder.deviceStatusTextView.text = "InActive"
        }

        holder.deviceDeleteBtn.setOnClickListener {
            listener.onItemClick(device)
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun setData(devices: MutableList<Device>) {
        this.devices.clear()
        this.devices = devices
    }

    interface OnAddBtnClickListener {
        fun onItemClick(device: Device)
    }
}
