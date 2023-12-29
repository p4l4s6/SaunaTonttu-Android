package tech.cognix.sauntatonttu.ui.activity.ui.home

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
import tech.cognix.sauntatonttu.ui.viewmodels.ViewModelFactory

class HomeViewModel : ViewModel {
    private var apiService: ApiService? = null

    constructor()
    constructor(apiService: ApiService) {
        this.apiService = apiService
    }


    fun startSession(saunaSession: SaunaSession) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiService?.createSaunaSession(
                        SaunaSession.StartSession(saunaSession.start_time!!)
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun stopSession(saunaSession: SaunaSession) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiService?.updateSaunaSession(
                        saunaSession.id!!, SaunaSession.StopSession(saunaSession.end_time!!)
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun addData(reading: Reading) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiService?.addReading(
                        Reading.AddData(
                            reading.session!!,
                            reading.device!!,
                            reading.pressure!!,
                            reading.temp!!,
                            reading.humidity!!,
                            reading.timestamp!!
                        )
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }


    fun initial(context: FragmentActivity): HomeViewModel {
        return ViewModelProvider(
            context,
            ViewModelFactory(context.apiClient().create(ApiService::class.java))
        )[HomeViewModel::class.java]
    }
}
