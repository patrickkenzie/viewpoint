package com.patrickkenzie.viewpoint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import com.google.android.gms.common.GoogleApiAvailability

import kotlinx.android.synthetic.main.activity_hosting.*
import kotlin.properties.Delegates

class HostingActivity : AppCompatActivity(), ConnectionClient.ConnectionObserver, CameraViewFragment.OnFragmentInteractionListener {
    var client: ConnectionClient by Delegates.notNull()

    override fun onRecordingError(err: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val fragmentTag = "fraggy"

    override fun onClientInit() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, CameraViewFragment.newInstance(""), fragmentTag)
                .commit()
    }

    override fun onConnectionCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(errorCode: Int) {
        val api = GoogleApiAvailability.getInstance()

        val dialog = api.getErrorDialog(this, errorCode, 1)

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)
        setSupportActionBar(toolbar)

        client = ConnectionClient(this, this, true)

        fab.setOnClickListener(this::startRecordingListener)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        client.start()
    }

    override fun onStop() {
        super.onStop()

        client.stop()
    }

    fun startRecordingListener(view: View) {
        // Camera
        val camera = supportFragmentManager.findFragmentByTag(fragmentTag) as CameraViewFragment
        camera.startRecording()
    }
}
