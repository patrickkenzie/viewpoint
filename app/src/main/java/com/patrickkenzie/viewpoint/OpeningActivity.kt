package com.patrickkenzie.viewpoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GoogleApiAvailability
import com.patrickkenzie.viewpoint.connections.ActiveHost
import com.patrickkenzie.viewpoint.connections.ActiveHostFragment
import com.patrickkenzie.viewpoint.connections.ActiveHostFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.activity_opening.*
import kotlinx.android.synthetic.main.content_opening.*
import kotlin.properties.Delegates

class OpeningActivity : AppCompatActivity(), ConnectionClient.ConnectionObserver, OnListFragmentInteractionListener {
    val PERMISSIONS_REQUEST = 1001;
    var client: ConnectionClient by Delegates.notNull()

    val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun permissionRationale(perm: String): String = when (perm) {
        Manifest.permission.WRITE_EXTERNAL_STORAGE -> "storage permissions are required to save video"
        Manifest.permission.ACCESS_COARSE_LOCATION -> "location permissions are required to connect to nearby hosts"
        else -> "without the required permissions some features may not work properly"
    }

    override fun onListFragmentInteraction(host: ActiveHost?) {
        Log.d(LOG_TAG, "host selected: " + host?.id)
    }

    override fun onClientInit() {
        Log.d(LOG_TAG, "onClientInit")
    }

    override fun onConnectionCreated() {
        Log.d(LOG_TAG, "onConnectionCreated")
    }

    override fun onConnectionFailed(errorCode: Int) {
        val api = GoogleApiAvailability.getInstance()

        val dialog = api.getErrorDialog(this, errorCode, 1)

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)
        setSupportActionBar(toolbar)

        client = ConnectionClient(this, this, false)
        fab.setOnClickListener(this::beginHosting)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_opening, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        if (requestRequiredPermissions()) {
            client.start()
        }
    }

    override fun onStop() {
        super.onStop()

        client.stop()
    }

    override fun onResume() {
        super.onResume()

        var list = hostList as ActiveHostFragment
        list.hostFound(ActiveHost("1234", "First Item"))
        list.hostFound(ActiveHost("4321", "Second Item"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PERMISSIONS_REQUEST -> requestRequiredPermissions()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                Log.d(LOG_TAG, "Permission result")

                val denied = permissions.filterIndexed { index, _ ->
                    grantResults[index] != PackageManager.PERMISSION_GRANTED
                }

                if (denied.isEmpty()) {
                    client.start()
                    return
                }

                val reAsk = denied.filter { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }

                val builder = AlertDialog.Builder(this)
                        .setNegativeButton("Cancel") { _, _ -> }

                fun buildMessage(items: Iterable<String>) =
                        items.map(this::permissionRationale).joinToString("; ", "Note: ", ".")

                if (reAsk.isEmpty()) {
                    builder.setMessage("${buildMessage(denied)}\nYou can re-enable permissions in settings")
                            .setPositiveButton("Open Settings") { _, _ ->
                                val settingsIntent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + this.packageName)
                                )

                                startActivityForResult(settingsIntent, PERMISSIONS_REQUEST)
                            }
                } else {
                    builder.setMessage(buildMessage(reAsk))
                            .setPositiveButton("Ok") { _, _ ->
                                ActivityCompat.requestPermissions(this, denied.toTypedArray(), PERMISSIONS_REQUEST)
                            }
                }

                builder.create()
                        .show()
            }
        }
    }

    fun requestRequiredPermissions(): Boolean {
        val missing = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            return true
        }

        ActivityCompat.requestPermissions(this, missing.toTypedArray(), PERMISSIONS_REQUEST)
        return false
    }

    fun beginHosting(view: View)  {
        // List of viewpoints
        // Start Session
        val intent = Intent(this, HostingActivity::class.java)

        startActivity(intent)
    }

}
