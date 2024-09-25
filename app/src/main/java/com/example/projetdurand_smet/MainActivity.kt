package com.example.projetdurand_smet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projetdurand_smet.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.LinkProperties
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import android.location.Location


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissions()

        binding.btnSubmit.setOnClickListener {
            val answer = binding.etAnswer.text.toString()
            val sharedPreferences = getSharedPreferences("QuestionPrefs", Context.MODE_PRIVATE)
            val correctAnswer = sharedPreferences.getString("answer", "")

            if (answer.equals(correctAnswer, ignoreCase = true)) {
                getLocation { location ->
                    val intent = Intent(this, SecondActivity::class.java)
                    intent.putExtra("location", location)
                    intent.putExtra("ipAddress", getIPAddress())
                    intent.putExtra("dnsServers", getDnsServers())
                    startActivity(intent)
                }
            } else {
                Snackbar.make(it, "Wrong answer, try again!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun getLocation(callback: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val locationString = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                    callback(locationString)
                } else {
                    callback("Location not available")
                }
            }
        } else {
            callback("Location permission not granted")
        }
    }

    private fun getIPAddress(): String {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (networkInterface in interfaces) {
            val addresses = networkInterface.inetAddresses
            for (address in addresses) {
                if (!address.isLoopbackAddress && address is InetAddress && address.hostAddress.indexOf(':') == -1) {
                    return address.hostAddress
                }
            }
        }
        return "Not available"
    }

    private fun getDnsServers(): String {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(network)
        val dnsServers = linkProperties?.dnsServers
        return dnsServers?.joinToString(", ") ?: "Not available"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted
            } else {
                Snackbar.make(binding.root, "All permissions are required to use this feature.", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}