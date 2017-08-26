package com.patrickkenzie.viewpoint

import android.os.Bundle
amport android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.common.GoogleApiAvailability
import com.patrickkenzie.viewpoint.video.Camera2VideoFragment

import kotlinx.android.synthetic.main.activity_hosting.*
import kotlin.properties.Delegates

class HostingActivity : AppCompatActivity(), ConnectionClient.ConnectionObserver {
    val fragmentTag = "fraggy"

    override fun onClientInit() {
        val trans = supportFragmentManager.beginTransaction()

        val video = Camera2VideoFragment()
        trans.add(R.id.container, video, fragmentTag)
        trans.commit()
    }

    override fun onConnectionCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(errorCode: Int) {
        val api = GoogleApiAvailability.getInstance()

        val dialog = api.getErrorDialog(this,  errorCode, 1)

        dialog.show()
    }

    var client: ConnectionClient by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)
        setSupportActionBar(toolbar)

        client = ConnectionClient(this.applicationContext, this, true)

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

    }
}
