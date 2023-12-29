package tech.cognix.sauntatonttu.utils
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1002
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkBluetoothPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            context as AppCompatActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun requestBluetoothPermission() {
        ActivityCompat.requestPermissions(
            context as AppCompatActivity,
            arrayOf(Manifest.permission.BLUETOOTH),
            BLUETOOTH_PERMISSION_REQUEST_CODE
        )
    }


    fun requestLocationAndBluetoothPermissions() {
        if (!checkLocationPermission()) {
            requestLocationPermission()
        }

        if (!checkBluetoothPermission()) {
            requestBluetoothPermission()
        }
    }
}
