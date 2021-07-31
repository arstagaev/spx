package com.revolve44.solarpanelx.ui.fragments.createstation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.ui.AddSolarStationActivity
import timber.log.Timber
import java.lang.Exception

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener {
    private var mMap: GoogleMap? = null
    private var marker: Marker? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var check = false
    private var MYLOCATION = LatLng(latitude, longitude)
    private lateinit var to_characteristics : Button



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = (childFragmentManager // *** change to childFM
            .findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)



        to_characteristics = view.findViewById(R.id.to_characteristics)

        to_characteristics.setOnClickListener {
            (activity as AddSolarStationActivity).gotoSecondPage()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        latitude = PreferenceMaestro.lat.toDouble()
        longitude = PreferenceMaestro.lon.toDouble()

        mMap = googleMap
        if (true.also { check = it }) {
            val resumedPosition = LatLng(latitude, longitude)
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(resumedPosition)
                    .draggable(true)
            )
            mMap!!.setOnMarkerDragListener(this) // bridge for connect marker with methods located below
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(resumedPosition)) // move camera to current position
        } else {
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(MYLOCATION)
                    .draggable(true)
            )
            mMap!!.setOnMarkerDragListener(this) // bridge for connect marker with methods located below
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(MYLOCATION)) // move camera to current position
        }
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        //setUpTracking();
        mMap!!.setOnMapClickListener { latLng ->
            // create marker
            val marker =
                MarkerOptions().position(LatLng(latitude, longitude)).title("Hello Maps")
            // adding marker
            mMap!!.addMarker(marker)
            // Creating a marker
            val markerOptions = MarkerOptions()
            // Setting the position for the marker
            markerOptions.position(latLng)
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
            // Clears the previously touched position
            mMap!!.clear()
            // Animating to the touched position
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            //Placing a marker on the touched position
            mMap!!.addMarker(markerOptions)
            // get coord
            latitude = latLng.latitude
            longitude = latLng.longitude

            saveCoordinations()

        }
    }

    private fun saveCoordinations(){
        //save coordinates
        try {
            PreferenceMaestro.lat = latitude.toFloat()
            PreferenceMaestro.lon = longitude.toFloat()
            Toast.makeText(activity, "lat: "+ String.format("%.3f", latitude)+" lon: "+String.format("%.3f", longitude), Toast.LENGTH_SHORT)
                .show()
        }catch (e: Exception){
            Timber.e("ERROR  ${e.message}")
        }

    }


    override fun onMarkerDragStart(marker: Marker) {
        //Toast.makeText(activity, "onMarkerDragStart", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDrag(marker: Marker) {
        //Toast.makeText(activity, "onMarkerDrag", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragEnd(marker: Marker) {
        MYLOCATION = marker.position
        //Toast.makeText(activity, "" + lol, Toast.LENGTH_SHORT).show()
    }

}
