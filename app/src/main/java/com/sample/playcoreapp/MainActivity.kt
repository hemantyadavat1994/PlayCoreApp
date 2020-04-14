package com.sample.playcoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        update()
    }

    override fun onResume() {
        super.onResume()
        resumeUpdate()
    }

    private fun update() {
        val updateManager = AppUpdateManagerFactory.create(applicationContext)
        updateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            scan_status.text = getString(R.string.completed_scanning)
            makeToast(getString(R.string.completed_scanning))
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                update_status.text = getString(R.string.found_update)
                makeToast(getString(R.string.found_update))
                updateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    200
                )
            }
        }
        updateManager.appUpdateInfo.addOnFailureListener {
            scan_status.text = getString(R.string.scan_failed)
            makeToast(getString(R.string.scan_failed))
        }
    }

    private fun resumeUpdate() {
        val updateManager = AppUpdateManagerFactory.create(this)
        updateManager.appUpdateInfo
            .addOnSuccessListener {
                scan_status.text = getString(R.string.completed_scanning)
                makeToast(getString(R.string.completed_scanning))
                if (it.updateAvailability() ==
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    update_status.text = getString(R.string.in_progress)
                    makeToast(getString(R.string.in_progress))
                    updateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        200
                    )
                }
            }
    }

    private fun makeToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }
}
