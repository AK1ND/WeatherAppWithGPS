package com.alex_kind.weatherappwithgps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alex_kind.weatherappwithgps.databinding.ActivityMainBinding
import com.alex_kind.weatherappwithgps.ui.currentWeatherFragment.NavigationFragmentCurrentWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    private var PERMISSION_ID = 1000

//    private val dataModel: DataModel by viewModels()
    private val dataModelCurrentVM: NavigationFragmentCurrentWeatherViewModel by viewModels()
    var retrofitBuilder = RetrofitCreator().getRetrofit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val navView: BottomNavigationView = bind.bottomNavView
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)


        animation()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        getLastLocation()

        bind.buttonRefreshToolbar.setOnClickListener {
            getLastLocation()
        }


    }

    private fun animation() {
        val constraintLayout: View = findViewById(R.id.string)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }


    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    getNewLocation()
                    if (location == null) {
                        getNewLocation()
                    } else {

                        val city = getCityName(location.latitude, location.longitude)
                        val country = getCountryName(location.latitude, location.longitude)
                        bind.tvCityName.text = "$city, $country"

                        //send CityName to observer for i can take it from fragment
//                        dataModel.cityName.value =
//                            getCityName(location.latitude, location.longitude)
                        dataModelCurrentVM.cityName.value =
                            getCityName(location.latitude, location.longitude)


                        //RESPONSE
                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                                val response = retrofitBuilder.weather(
                                    city,
                                    "6e298e72d16587b721abb30bbf7c721a",
                                    "metric"
                                ).awaitResponse()
                                if (response.isSuccessful) {
                                    val data = response.body()!!
                                    Log.d("CW", "Response is successful")
                                    dataModelCurrentVM.temp.value =
                                        data.list[0].main.temp.toString() + "°C"
                                    dataModelCurrentVM.description.value =
                                        data.list[0].weather[0].description
                                    val windSpeed = data.list[0].wind.speed
                                    val windSpeedInKM = data.list[0].wind.speed * 3.6
                                    dataModelCurrentVM.wind.value =
                                        windSpeed.toString() + " m/s" + "\n(" +
                                                String.format("%.2f", windSpeedInKM) + " km/h)"
                                    dataModelCurrentVM.humidity.value =
                                        data.list[0].main.humidity.toString() + "%"
                                    dataModelCurrentVM.iconID.value = data.list[0].weather[0].icon
                                }
                            } catch (e: Exception) {
                                Log.d("error response", "ERROR RESPONSE")
                                Toast.makeText(
                                    this@MainActivity,
                                    "ERROR CONNECTION TO API", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun getNewLocation() {
        locationRequest = com.google.android.gms.location.LocationRequest.create() //???
        locationRequest.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallBack, it
            )
        }

    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
        }
    }

    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getCityName(lat: Double, long: Double): String {
        var cityname = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)

        cityname = address.get(0).locality

        return cityname
    }

    private fun getCountryName(lat: Double, long: Double): String {
        var countryName = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)

        countryName = address.get(0).countryName

        return countryName
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the PERMISSION")
            }
        }
    }

}