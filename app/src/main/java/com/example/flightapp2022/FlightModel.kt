package com.example.flightapp2022

/**
 * Created by sergio on 07/11/2021
 * All rights reserved GoodBarber
 */
data class FlightModel (val icao24: String,
                        val firstSeen: Long,
                        val estDepartureAirport: String,
                        val lastSeen: Long,
                        val estArrivalAirport: String,
                        val callsign: String,
                        val estDepartureAirportHorizDistance: Int,
                        val estDepartureAirportVertDistance: Int,
                        val estArrivalAirportHorizDistance: Int,
                        val estArrivalAirportVertDistance: Int,
                        val departureAirportCandidatesCount: Int,
                        val arrivalAirportCandidatesCount: Int,
                        val path: Array<Any>)