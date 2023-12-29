package tech.cognix.sauntatonttu.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.cognix.sauntatonttu.network.ApiService
import tech.cognix.sauntatonttu.ui.activity.ui.dashboard.DashboardViewModel
import tech.cognix.sauntatonttu.ui.activity.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
//            return MainViewModel(apiService) as T
//        }

        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(apiService) as T
        }
        if(modelClass.isAssignableFrom(DeviceViewModel::class.java)){
            return DeviceViewModel(apiService) as T
        }
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(apiService) as T
        }
        if(modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            return DashboardViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
