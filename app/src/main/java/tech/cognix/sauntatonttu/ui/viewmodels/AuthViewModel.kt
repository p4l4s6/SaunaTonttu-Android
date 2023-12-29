package tech.cognix.sauntatonttu.ui.viewmodels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tech.cognix.sauntatonttu.models.User
import tech.cognix.sauntatonttu.network.ApiClient.apiClient
import tech.cognix.sauntatonttu.network.ApiService
import tech.cognix.sauntatonttu.network.Resource


class AuthViewModel : ViewModel {
    private var apiService: ApiService? = null

    constructor()
    constructor(apiService: ApiService) {
        this.apiService = apiService
    }


    fun signup(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val data = apiService?.signup(
            User.SignUp(
                user.last_name,
                user.first_name,
                user.email,
                user.password,
                user.confirm_password
            )
        )
        try {
            emit(Resource.success(data = data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occured"))
        }
    }

    fun login(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(data = apiService?.login(User.Login(user.email, user.password)))
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(data = apiService?.getProfile())
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }


    fun initial(context: FragmentActivity): AuthViewModel {
        return ViewModelProvider(
            context,
            ViewModelFactory(context.apiClient().create(ApiService::class.java))
        )[AuthViewModel::class.java]
    }
}
