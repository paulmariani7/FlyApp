package com.example.flightapp2022

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightListMapModel : ViewModel() {

    private val flightListLiveData = MutableLiveData< FligthIndividualModel >()


    fun setFlightListLiveData(data: FligthIndividualModel?){
        flightListLiveData.value = data
    }

    fun getFlightListLiveData(): MutableLiveData<FligthIndividualModel>{
        return flightListLiveData
    }


    fun doRequestFlight( icao: String, time: String) {

        viewModelScope.launch {

            val url = "https://opensky-network.org/api/tracks/all"
            val params = HashMap<String, String>()
            params.put("icao24", icao.toString())
            params.put("time", time.toString())


            val result = withContext(Dispatchers.IO) {
                RequestManager.getSuspended(url, params)
            }
            if (result != null) {
                //Log.i("VUELO INDIVIDUAL", result)

                val fly = object : TypeToken< FligthIndividualModel >(){}.type
               val coordinates = Gson().fromJson<FligthIndividualModel>(result,fly)
                setFlightListLiveData(coordinates)
            } else {
                Log.e("REQUEST", "ERROR NO RESULT")
            }

        }

    }
}