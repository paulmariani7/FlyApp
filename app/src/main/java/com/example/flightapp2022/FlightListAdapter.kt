package com.example.flightapp2022

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.Maps

/**
 * Created by sergio on 25/10/2022
 * All rights reserved GoodBarber
 */
class FlightListAdapter(val flightList: List<FlightModel>, val cellClickListener: OnCellClickListener) :
    RecyclerView.Adapter<FlightListAdapter.FlightListCellViewHolder>() {

    interface OnCellClickListener {
        fun onCellClicked(flightModel: FlightModel)

    }

    fun changeActivity(context: Context, arrival: String, departure:String , lastSeen:String , icao24:String) {
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra("arrival", arrival)
        intent.putExtra("departure", departure)
        intent.putExtra("lastSeen", lastSeen)
        intent.putExtra("icao24", icao24)
        context.startActivity(intent)
    }


    class FlightListCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightListCellViewHolder {
        Log.i("CELL", "onCreateViewHolder")
        val cell = FlightInfoCell(parent.context)
        cell.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return FlightListCellViewHolder(cell)
    }

    override fun onBindViewHolder(holder: FlightListCellViewHolder, position: Int) {
        Log.i("CELL", "onBindViewHolder with position $position")

        val flight = flightList[position]
        val cell = holder.itemView as FlightInfoCell
        cell.setBackgroundColor(if (position % 2 == 0) Color.YELLOW else Color.GRAY)
        cell.bindData(flight)
        cell.setOnClickListener {
            Log.i("click en celda", "$flight")
            val context = holder.itemView.getContext()
            val arrive = flight.estArrivalAirport
            val departure = flight.estDepartureAirport
            val lastSeen = flight.lastSeen
            val icao24 = flight.icao24



            Log.i("prueba", "$arrive, $departure ")
           changeActivity(context,"$arrive" ,"$departure" , "$lastSeen", "$icao24");

            cellClickListener.onCellClicked(flight)
        }
    }

    override fun getItemCount(): Int {
        return flightList.size
    }
}