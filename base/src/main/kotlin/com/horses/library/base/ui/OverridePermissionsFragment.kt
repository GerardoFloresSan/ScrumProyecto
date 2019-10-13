package com.horses.library.base.ui

import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.widget.Toast

/**
 * @author @briansalvattore on 5/10/2018.
 */
abstract class OverridePermissionsFragment : Fragment() {

    @Suppress("PrivatePropertyName")
    private val NEEDED_PERMISSIONS = 2603

    private val permissionsNeverAsk: ArrayList<String> = ArrayList()
    private var permissionsNeed: ArrayList<String> = ArrayList()
    private var isCompleted = false

    fun requestPermissions(vararg arrays: String) {
        permissionsNeed.clear()

        arrays.filter {
            ActivityCompat.checkSelfPermission(activity?.applicationContext!!, it) != PackageManager.PERMISSION_GRANTED
        }.forEach { permissionsNeed.add(it) }

        if (arrays.size == permissionsNeverAsk.size) return

        if (permissionsNeed.size > 0) {
            ActivityCompat.requestPermissions(activity!!, permissionsNeed.toTypedArray(), NEEDED_PERMISSIONS)
        }
        else {
            permissionGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == NEEDED_PERMISSIONS) {
            var success = true

            grantResults.forEach {  success = success && it == 0 }

            if (success) permissionGranted()
            else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionsNeed.filter {
                        !shouldShowRequestPermissionRationale(it)
                    }.mapTo(permissionsNeverAsk) { it }
                }

                if (!permissions.isEmpty() && !isCompleted) {
                    permissionNeverAsk(permissionsNeverAsk.toTypedArray())
                }

                val permissionsDenied: List<String> = permissionsNeed.indices.filter {
                    grantResults[it] != PackageManager.PERMISSION_GRANTED
                }.map { permissionsNeed[it] }

                if (permissionsNeverAsk.size != permissionsDenied.size) {
                    permissionDenied(permissionsDenied.toTypedArray())
                }
                else {
                    isCompleted = true
                }
            }
            return
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun permissionGranted() {
        if (activity == null) return
       // Toast.makeText(activity?.applicationContext!!, "Permissions Granted", Toast.LENGTH_SHORT).show()
    }

    open fun permissionDenied(denied: Array<String>) {
        if (activity == null) return
       // Toast.makeText(activity?.applicationContext!!, "Permissions Denied", Toast.LENGTH_SHORT).show()
    }

    open fun permissionNeverAsk(denied: Array<String>) {
        if (activity == null) return
        //Toast.makeText(activity?.applicationContext!!, "Permissions never ask", Toast.LENGTH_SHORT).show()
    }
}