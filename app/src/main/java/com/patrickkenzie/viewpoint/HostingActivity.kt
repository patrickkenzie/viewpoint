package com.patrickkenzie.viewpoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_hosting.*

class HostingActivity : AppCompatActivity() {
    var client: ConnectionClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)
        setSupportActionBar(toolbar)

        client = ConnectionClient(this.applicationContext, true)

        fab.setOnClickListener(this::startRecordingListener)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        client?.start()
    }

    override fun onStop() {
        super.onStop()

        client?.stop()
    }

    fun startRecordingListener(view: View) {
        // Camera

    }
}
