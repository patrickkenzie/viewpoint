package com.patrickkenzie.viewpoint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.common.GoogleApiAvailability
import com.patrickkenzie.viewpoint.connections.ActiveHost
import com.patrickkenzie.viewpoint.connections.ActiveHostFragment
import com.patrickkenzie.viewpoint.connections.ActiveHostFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.activity_opening.*
import kotlinx.android.synthetic.main.content_opening.*
import kotlin.properties.Delegates

class OpeningActivity : AppCompatActivity(), ConnectionClient.ConnectionObserver, OnListFragmentInteractionListener {
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

        val dialog = api.getErrorDialog(this,  errorCode, 1)

        dialog.show()
    }

    var endpointName = ""
    var endpointId = ""

    var client: ConnectionClient by Delegates.notNull()

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

        client.start()
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

    fun beginHosting(view: View)  {
        // List of viewpoints
        // Start Session
        val intent = Intent(this, HostingActivity::class.java)

        startActivity(intent)
    }

}
