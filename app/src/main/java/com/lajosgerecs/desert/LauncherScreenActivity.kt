package com.lajosgerecs.desert

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_launcher_screen.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class LauncherScreenActivity : AppCompatActivity() {
    var clockThread: Thread? = null;
    val timeFormat = SimpleDateFormat("HH:mm")
    val dateFormat = SimpleDateFormat("E, dd MMM")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        setContentView(R.layout.activity_launcher_screen)
        val openAppsButton: ImageButton = findViewById(R.id.open_apps_button)


        clockThread = createClockUpdaterThread()

        clockThread!!.start()

        refreshApps()


        openAppsButton.setOnClickListener {
            val activity2Intent = Intent(applicationContext, AppListActivity::class.java)
            startActivity(activity2Intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!this.clockThread!!.isAlive()) {
            this.clockThread = createClockUpdaterThread()
        }
        refreshApps()

    }

    private fun refreshApps() {
        thread(start = true) {
            Log.e(this.localClassName, "Loading app list");
            if (!AppListCache.calculating){
                AppListCache.calculating = true;
                AppListCache.update(this)
                AppListCache.calculating = false;
                Log.e(this.localClassName, "Loading app list finished " + AppListCache.items.size);
            } else {
                Log.e(this.localClassName, "Loading app list already running ");
            }
        }
    }

    private fun createClockUpdaterThread(): Thread {
        val timeView = findViewById<TextView>(R.id.timeTextView)
        return object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {

                        val now = Date()
                        runOnUiThread {
                            timeView.setText(timeFormat.format(now))
                            dateTextView.setText(dateFormat.format(now))
                        }
                        sleep(1000)
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
    }

}