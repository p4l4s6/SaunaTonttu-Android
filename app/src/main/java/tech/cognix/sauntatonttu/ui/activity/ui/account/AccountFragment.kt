package tech.cognix.sauntatonttu.ui.activity.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import tech.cognix.sauntatonttu.databinding.FragmentAccountBinding
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.activity.ui.dashboard.DashboardViewModel
import tech.cognix.sauntatonttu.ui.adapters.SaunaSessionAdapter
import tech.cognix.sauntatonttu.ui.viewmodels.AuthViewModel
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progressBarUtils: ProgressBarUtils
    private var networkUtils: NetworkUtils = NetworkUtils()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authViewModel = AuthViewModel().initial(requireActivity())
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
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
        _binding!!.btnHealthTips.setOnClickListener{
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show()
        }
        _binding!!.btnNotification.setOnClickListener{
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show()
        }

        getProfile()
    }

    private fun getProfile() {
        progressBarUtils.showProgressBar()
        authViewModel.getProfile().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBarUtils.hideProgressBar()
                        if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                            _binding!!.profileName.text = String.format(
                                "%s %s",
                                it.data.body()!!.data!!.first_name,
                                it.data.body()!!.data!!.last_name
                            )
                            _binding!!.profileEmail.text =it.data.body()!!.data!!.email
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