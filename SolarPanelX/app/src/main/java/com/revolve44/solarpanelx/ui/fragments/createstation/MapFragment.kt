//package com.revolve44.solarpanelx.ui.fragments.createstation
//
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.fragment.app.Fragment
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.revolve44.solarpanelx.R
//import com.revolve44.solarpanelx.activity.AddSolarStationActivity
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import timber.log.Timber
//import java.lang.Exception
//
//class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
//    GoogleMap.OnMarkerDragListener {
//    // Store instance variables
//   // private var title: String? = null
//   // private var page = 0
//    private var mMap: GoogleMap? = null
//   // var lol: LatLng? = null
//    var marker: Marker? = null
//    var latitude = 0.0
//    var longitude = 0.0
////    var NominalPower = 0.0f
////    var Latitude: String? = null
////    var Longitude: String? = null
//    var check = false
//    var MYLOCATION = LatLng(latitude, longitude)
////    var Loader: LinearLayout? = null
////    //var textView: TextView? = null
////    var inputnominalpower: EditText? = null
////    var tempFahrenheit = false
////    var checkImperial: CheckBox? = null
////    lateinit var toCharacteristics : Button
//    lateinit var to_characteristics : Button
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = (childFragmentManager // *** change to childFM
//            .findFragmentById(R.id.map) as SupportMapFragment?)!!
//        mapFragment.getMapAsync(this)
//
//        to_characteristics = view.findViewById(R.id.to_characteristics)
//
//        //toCharacteristics = view.findViewById(R.id.to_characteristics)
//        to_characteristics.setOnClickListener {
//            (activity as AddSolarStationActivity).gotoSecondPage() }
//
//
//
//
//    }
//
//    // Inflate the view for the fragment based on layout XML
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        val view =
////            inflater.inflate(R.layout.fragmentlocation_map, container, false)
////        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
////
////        return view
////    }
//    ////////////////////
//    /** ШАБЛОННЫЙ ГУГОЛОВСКИЙ КОМЕНТАРИЙ ПО ПОВОДУ ИХ КАРТ, РЕЛАКС. донт ворри
//     * просто оставил почитать
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    override fun onMapReady(googleMap: GoogleMap) {
//
//        latitude = PreferenceMaestro.lat.toDouble()
//        longitude = PreferenceMaestro.lon.toDouble()
//
//        mMap = googleMap
//        if (true.also { check = it }) {
//            val resumedPosition = LatLng(latitude, longitude)
//            marker = googleMap.addMarker(
//                MarkerOptions()
//                    .position(resumedPosition)
//                    .draggable(true)
//            )
//            mMap!!.setOnMarkerDragListener(this) // bridge for connect marker with methods located below
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(resumedPosition)) // move camera to current position
//        } else {
//            marker = googleMap.addMarker(
//                MarkerOptions()
//                    .position(MYLOCATION)
//                    .draggable(true)
//            )
//            mMap!!.setOnMarkerDragListener(this) // bridge for connect marker with methods located below
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(MYLOCATION)) // move camera to current position
//        }
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.uiSettings.isMyLocationButtonEnabled = true
//        //setUpTracking();
//        mMap!!.setOnMapClickListener { latLng ->
//            // create marker
//            val marker =
//                MarkerOptions().position(LatLng(latitude, longitude)).title("Hello Maps")
//            // adding marker
//            mMap!!.addMarker(marker)
//            // Creating a marker
//            val markerOptions = MarkerOptions()
//            // Setting the position for the marker
//            markerOptions.position(latLng)
//            // Setting the title for the marker.
//            // This will be displayed on taping the marker
//            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
//            // Clears the previously touched position
//            mMap!!.clear()
//            // Animating to the touched position
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//            //Placing a marker on the touched position
//            mMap!!.addMarker(markerOptions)
//            // get coord
//            latitude = latLng.latitude
//            longitude = latLng.longitude
//
//            saveCoordinations()
//
//        }
//    }
//
//    private fun saveCoordinations(){
//        //save coordinates
//        try {
//            PreferenceMaestro.lat = latitude.toFloat()
//            PreferenceMaestro.lon = longitude.toFloat()
//            Toast.makeText(activity, "lat: "+ String.format("%.3f", latitude)+" lon: "+String.format("%.3f", longitude), Toast.LENGTH_SHORT)
//                .show()
//        }catch (e: Exception){
//            Timber.e("ERROR  ${e.message}")
//        }
//
//    }
//
//
//    override fun onMarkerDragStart(marker: Marker) {
//        //Toast.makeText(activity, "onMarkerDragStart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onMarkerDrag(marker: Marker) {
//        //Toast.makeText(activity, "onMarkerDrag", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onMarkerDragEnd(marker: Marker) {
//        MYLOCATION = marker.position
//        //Toast.makeText(activity, "" + lol, Toast.LENGTH_SHORT).show()
//    }
//
////    companion object {
////        // newInstance constructor for creating fragment with arguments
////        @JvmStatic
////        fun newInstance(page: Int, title: String?): MapFragment {
////            val fragmentFirst = MapFragment()
////            val args = Bundle()
////            args.putInt("someInt", page)
////            args.putString("someTitle", title)
////            fragmentFirst.arguments = args
////            return fragmentFirst
////        }
////    }
//}
