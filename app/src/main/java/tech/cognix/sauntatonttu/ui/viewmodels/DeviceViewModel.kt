package tech.cognix.sauntatonttu.ui.viewmodels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tech.cognix.sauntatonttu.models.Device
import tech.cognix.sauntatonttu.models.User
import tech.cognix.sauntatonttu.network.ApiClient.apiClient
import tech.cognix.sauntatonttu.network.ApiService
import tech.cognix.sauntatonttu.network.Resource


class DeviceViewModel : ViewModel {
    private var apiService: ApiService? = null

    constructor()
    constructor(apiService: ApiService) {
        this.apiService = apiService
    }


    fun addDevice(device: Device) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val data = apiService?.addDevice(
            Device.AddDevice(
                device.mac_address,
                device.mac_address,
                true
            )
        )
        try {
            emit(Resource.success(data = data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun deleteDevice(deviceID: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val data = apiService?.deleteDevice(deviceID)
        try {
            emit(Resource.success(data = data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }


    fun getDevices() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(data = apiService?.getDevices())
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }


    fun initial(context: FragmentActivity): DeviceViewModel {
        return ViewModelProvider(
            context,
            ViewModelFactory(context.apiClient().create(ApiService::class.java))
        )[DeviceViewModel::class.java]
    }
}
