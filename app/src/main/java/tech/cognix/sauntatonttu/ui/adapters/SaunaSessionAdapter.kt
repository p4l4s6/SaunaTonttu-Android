package tech.cognix.sauntatonttu.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.utils.DateTimeUtils

class SaunaSessionAdapter(
    private val context: Context,
    private var sessions: MutableList<SaunaSession>,
    private val listener: OnAddBtnClickListener
) :
    RecyclerView.Adapter<SaunaSessionAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sessionDate: TextView = itemView.findViewById(R.id.session_date)
        val startTime: TextView = itemView.findViewById(R.id.start_time)
        val endTime: TextView = itemView.findViewById(R.id.end_time)
        val saunaDetailsBtn: ImageView = itemView.findViewById(R.id.session_details_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_sauna_session, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val session = sessions[position]
        holder.sessionDate.text = DateTimeUtils().getDateFromServer(session.start_time)
        holder.startTime.text = String.format("Start Time: %s", DateTimeUtils().getTimeFromServer(session.start_time))
        holder.endTime.text = String.format("End Time: %s", DateTimeUtils().getTimeFromServer(session.end_time))
        holder.saunaDetailsBtn.setOnClickListener {
            listener.onItemClick(session)
        }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    fun setData(sessions: MutableList<SaunaSession>) {
        this.sessions.clear()
        this.sessions = sessions
    }

    interface OnAddBtnClickListener {
        fun onItemClick(session: SaunaSession)
    }
}
