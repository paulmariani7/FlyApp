package com.example.flightapp2022

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.flightapp2022.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var viewModel: FlightListMapModel






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlightListMapModel::class.java)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lastSeen = intent.getStringExtra("lastSeen")
        val icao24 = intent.getStringExtra("icao24")

        //here i do request for getting coordinates of the flight
        viewModel.doRequestFlight("$icao24", "$lastSeen")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val arrival = intent.getStringExtra("arrival")
        val departure = intent.getStringExtra("departure")
        Log.i("departureP" ,"$departure, $arrival")

        viewModel.getFlightListLiveData().observe(this){ model ->
          //  Log.i("data PARIS",model.toString())

            val first = model.path.first()
            val last = model.path.last()
            val line = PolylineOptions();
                for ( a in model.path ){
                var position = LatLng(a[1].toString().toDouble(), a[2].toString().toDouble())
                line.add(LatLng(a[1].toString().toDouble(), a[2].toString().toDouble()))
                    .width(5f)
                    .color(Color.RED);

                if(a == first){
                    mMap.addMarker(MarkerOptions().position(position).title(""))
                }
                    if(a == last){
                        mMap.addMarker(MarkerOptions().position(position).title(""))
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
            }

            googleMap.addPolyline(line);





        }


        // Add a marker in Sydney and move the camera

    }
}