package com.example.projetdurand_smet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projetdurand_smet.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location = intent.getStringExtra("location")
        val ipAddress = intent.getStringExtra("ipAddress")
        val dnsServers = intent.getStringExtra("dnsServers")

        binding.tvLocation.text = "Localisation actuelle : $location"
        binding.tvIpAddress.text = "IP Address: $ipAddress"
        binding.tvDnsServers.text = "DNS Servers: $dnsServers"
    }
}