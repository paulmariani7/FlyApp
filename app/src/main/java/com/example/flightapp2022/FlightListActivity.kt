package com.example.flightapp2022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlightListActivity : AppCompatActivity() , FlightListAdapter.OnCellClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val begin = intent.getLongExtra("BEGIN", 0)
        val end = intent.getLongExtra("END", 0)
        val isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        val icao = intent.getStringExtra("ICAO")

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)

        Log.i("MAIN ACTIVITY", "begin = $begin \n end = $end \n icao = $icao \n is arrival = $isArrival")

        // DO NOT DO REQUEST IN ACTIVITY LIKE THE COMMENT BELOW
        //RequestManager.get("https://google.fr", HashMap())

        viewModel.doRequest(begin, end, isArrival, icao!!)

        viewModel.getFlightListLiveData().observe(this, Observer {
            //findViewById<TextView>(R.id.textView).text = it.toString()

            //Récupérer le recyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            // Attacher un Adapter
            recyclerView.adapter = FlightListAdapter(it, this)
            // Attacher un LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        })
    }

    override fun onCellClicked(flightModel: FlightModel) {
        Log.i("CELL", "cell clicked $flightModel")
    }
}