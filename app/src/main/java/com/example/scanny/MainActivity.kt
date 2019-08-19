package com.example.scanny

import android.bluetooth.BluetoothManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import android.util.Log
import kotlin.concurrent.thread
import android.Manifest
import android.bluetooth.le.*
import android.content.pm.PackageManager

class MainActivity : AppCompatActivity() {

    private val bleScanner = object : ScanCallback() {

    }       override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.d("ScanDeviceActivity", "onScanResult(): ${result?.device?.address} - ${result?.device?.name}")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thread(start = true) {
            println("${Thread.currentThread()} has run.")
            val bman = applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            val scanner = bman.adapter.bluetoothLeScanner
            val filters: List<ScanFilter> = listOf(ScanFilter.Builder().setDeviceName("iSensor ").build())
            val settings = ScanSettings.Builder().setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
            when (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PackageManager.PERMISSION_GRANTED -> scanner.startScan(filters,settings, bleScanner)
                //PackageManager.PERMISSION_GRANTED -> scanner.startScan(bleScanner)
                else -> requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }

        }

    }
}
