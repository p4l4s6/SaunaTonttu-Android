package tech.cognix.sauntatonttu.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import tech.cognix.sauntatonttu.models.Dashboard
import tech.cognix.sauntatonttu.models.Device
import tech.cognix.sauntatonttu.models.Reading
import tech.cognix.sauntatonttu.models.SaunaSession
import tech.cognix.sauntatonttu.models.User
import tech.cognix.sauntatonttu.response.BaseListResponse
import tech.cognix.sauntatonttu.response.BaseResponse

interface ApiService {

    @POST("v1/auth/signup/")
    suspend fun signup(@Body user: User.SignUp): Response<BaseResponse<User>>

    @POST("v1/auth/login/")
    suspend fun login(@Body user: User.Login): Response<BaseResponse<User>>

    @GET("v1/auth/profile/")
    suspend fun getProfile(): Response<BaseResponse<User>>

    @GET("v1/sauna/mobile/dashboard/")
    suspend fun getDashboard(): Response<BaseResponse<Dashboard>>

    @GET("v1/sauna/mobile/device/")
    suspend fun getDevices(): Response<BaseResponse<BaseListResponse<Device>>>

    @GET("v1/sauna/mobile/saunasession/")
    suspend fun getSaunaSessions(): Response<BaseResponse<BaseListResponse<SaunaSession>>>

    @POST("v1/sauna/mobile/saunasession/")
    suspend fun createSaunaSession(@Body saunaSession: SaunaSession.StartSession): Response<BaseResponse<SaunaSession>>

    @PATCH("v1/sauna/mobile/saunasession/{id}/")
    suspend fun updateSaunaSession(
        @Path("id") id: Int,
        @Body saunaSession: SaunaSession.StopSession
    ): Response<BaseResponse<SaunaSession>>

    @POST("v1/sauna/mobile/reading/")
    suspend fun addReading(@Body reading: Reading.AddData): Response<BaseResponse<Reading>>

    @POST("v1/sauna/mobile/device/")
    suspend fun addDevice(@Body device: Device.AddDevice): Response<BaseResponse<Device>>

    @DELETE("v1/sauna/mobile/device/{id}/")
    suspend fun deleteDevice(@Path("id") id: Int): Response<BaseResponse<Device>>


}