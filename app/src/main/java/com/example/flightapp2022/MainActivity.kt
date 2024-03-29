package com.example.flightapp2022

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var beginDateLabel: TextView
    private lateinit var endDateLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        beginDateLabel = findViewById<TextView>(R.id.from_date)
        endDateLabel = findViewById<TextView>(R.id.to_date)

        beginDateLabel.setOnClickListener { showDatePickerDialog(MainViewModel.DateType.BEGIN) }
        endDateLabel.setOnClickListener { showDatePickerDialog(MainViewModel.DateType.END) }

        val airportSpinner = findViewById<Spinner>(R.id.airport_spinner)
        val airportSpinnerDestination = findViewById<Spinner>(R.id.airport_spinner2)

        viewModel.getAirportNamesListLiveData().observe(this) {
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, it
            )

            airportSpinner.adapter = adapter
            airportSpinnerDestination.adapter = adapter
        }


        viewModel.getBeginDateLiveData().observe(this) {
            beginDateLabel.text = Utils.dateToString(it.time)
        }

        viewModel.getEndDateLiveData().observe(this) {
            endDateLabel.text = Utils.dateToString(it.time)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            // Récupérer données pour la requête
                // Date de début
            val begin = viewModel.getBeginDateLiveData().value!!.timeInMillis / 1000
                // Date de fin
            val end = viewModel.getEndDateLiveData().value!!.timeInMillis / 1000
                // Airport
            val selectedAirportIndex = airportSpinner.selectedItemPosition
            val selectedAirportIndexDestionation = airportSpinnerDestination.selectedItemPosition

            val airportDestination = viewModel.getAirportListLiveData().value!![selectedAirportIndexDestionation]
            val airport = viewModel.getAirportListLiveData().value!![selectedAirportIndex]

            val icao = airport.icao
            var icaoDestatination = airportDestination.icao
                // Depart ou arrivée
            val isArrival = findViewById<Switch>(R.id.airport_switch).isChecked


            // Ouvrir une nouvelle activité avec les infos de la requête

            val intent = Intent(this, FlightListActivity::class.java)

            intent.putExtra("BEGIN",begin)
            intent.putExtra("END",end)
            intent.putExtra("IS_ARRIVAL",isArrival)
            intent.putExtra("ICAO",icao)
            intent.putExtra("ICAODESTAINATION",icaoDestatination)

            startActivity(intent)
        }


    }

    // open date picker dialog
    private fun showDatePickerDialog(dateType: MainViewModel.DateType) {
        // Date Select Listener.
        val dateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.updateCalendarLiveData(dateType, calendar)
            }

        val currentCalendar = if (dateType == MainViewModel.DateType.BEGIN) viewModel.getBeginDateLiveData().value else viewModel.getEndDateLiveData().value

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentCalendar!!.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}