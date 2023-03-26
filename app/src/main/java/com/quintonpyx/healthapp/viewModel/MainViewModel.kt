package com.quintonpyx.healthapp.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quintonpyx.healthapp.model.ResponseObject
import com.quintonpyx.healthapp.networking.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
class MainViewModel {
    private val _foodData = MutableLiveData<ResponseObject>()
    val foodData: LiveData<ResponseObject> get() = _foodData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getFoodData(food: String) {

        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getFood(key= food)

        // Send API request using Retrofit
        client.enqueue(object : Callback<ResponseObject> {

            override fun onResponse(
                call: Call<ResponseObject>,
                response: Response<ResponseObject>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _foodData.postValue(responseBody)
            }

            override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

        })
    }

    private fun onError(inputMessage: String?) {

        val message = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}