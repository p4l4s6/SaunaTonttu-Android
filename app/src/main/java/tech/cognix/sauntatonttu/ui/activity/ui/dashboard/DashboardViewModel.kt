package tech.cognix.sauntatonttu.ui.activity.ui.dashboard

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tech.cognix.sauntatonttu.models.Reading
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.network.ApiClient.apiClient
import tech.cognix.sauntatonttu.network.ApiService
import tech.cognix.sauntatonttu.network.Resource
import tech.cognix.sauntatonttu.ui.activity.ui.home.HomeViewModel
import tech.cognix.sauntatonttu.ui.viewmodels.ViewModelFactory

class DashboardViewModel : ViewModel {
    private var apiService: ApiService? = null

    constructor()
    constructor(apiService: ApiService) {
        this.apiService = apiService
    }


    fun getSessions() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiService?.getSaunaSessions()
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun getDashboard() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiService?.getDashboard()
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }


    fun initial(context: FragmentActivity): DashboardViewModel {
        return ViewModelProvider(
            context,
            ViewModelFactory(context.apiClient().create(ApiService::class.java))
        )[DashboardViewModel::class.java]
    }
}
