package com.patrickkenzie.viewpoint

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_opening.*

class OpeningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(this::startHosting)
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

    fun startHosting(view: View)  {
        // Spinner
        // List of viewpoints
        // Start Session
        Snackbar.make(view, "Test", Snackbar.LENGTH_SHORT)
        Snackbar.make(view, "Looking for other viewpoints...", Snackbar.LENGTH_LONG)
                .show()
    }

    fun startLooking(view: View) {
        // Spinner
    }
}
