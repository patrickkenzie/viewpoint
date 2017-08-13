package com.patrickkenzie.viewpoint

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_hosting.*

class HostingActivity : AppCompatActivity() {
    var client: ConnectionClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)
        setSupportActionBar(toolbar)

        client = ConnectionClient(this.applicationContext, true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
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
}
