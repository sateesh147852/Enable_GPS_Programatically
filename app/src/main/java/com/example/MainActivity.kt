package com.example

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MAinActiity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btGet.setOnClickListener {
            displayLocationPopup()
        }
    }

    private fun displayLocationPopup() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)


        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())


        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                //setupLocationListener()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this, 999
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK && requestCode == 999) {
            displayLocationPopup()
        }
        else if (resultCode == RESULT_OK && requestCode == 999){
            Toast.makeText(this,"Location is enabled",Toast.LENGTH_SHORT).show()
        }
    }


}