package com.revolve44.solarpanelx.feature_modules.lightsensor

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.revolve44.solarpanelx.BuildConfig
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.ui.MainActivity
import java.lang.Exception


class LightSensorActivity : AppCompatActivity(), SensorEventListener  {

//    val sensorManager: SensorManager by lazy {
//        getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    }

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    lateinit var viewModel : LightSensorViewModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lightsensor_activity_main)

        viewModel = ViewModelProvider(this).get(LightSensorViewModel::class.java)

        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()

        ft.add(R.id.container_main, FragmentLightSensor.create(), FragmentLightSensor.FRAGMENT_TAG)
        ft.commit()


//        var mp : MediaPlayer = MediaPlayer.create(this,R.raw.airplanebeep_1sec)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            mp.playbackParams.speed = 2F
//        }
//        mp.syncParams
//        mp.start()
//        var fragment = supportFragmentManager.findFragmentByTag(FragmentLightSensor.FRAGMENT_TAG) as FragmentLightSensor
//        fragment.exp()

//        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
//
//        if (lightSensor == null) {
//            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show()
//            //finish()
//        }

        // max value for light sensor

        // max value for light sensor
        //maxValue = lightSensor.getMaximumRange()

        Log.d("xsensor","Started! ")
        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        try {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)


        }catch (e:Exception){

            viewModel.lighter.value = 0F
        }

        if(lightSensor == null ){
            showWarningNoSensor()
        }

        if(lightSensor==null && BuildConfig.DEBUG ){
            //Toast.makeText(applicationContext, "Your Phone not have light sensor",Toast.LENGTH_LONG).show()

            // Dummy GENERATOR
//            var a = 1
//            val timer = object: CountDownTimer(1000000, 500) {
//                override fun onTick(millisUntilFinished: Long) {
//                    // do something
//                    viewModel.lighter.value = a.toFloat()
//                    a++
//                    //Toast.makeText(applicationContext,"range max"+lightSensor!!.maximumRange.toString(),Toast.LENGTH_SHORT).show()
//                }
//                override fun onFinish() {
//                    // do something
//                }
//            }
//            timer.start()

        }

        Log.d("xs",lightSensor.toString()+" and "+sensorManager.toString())

        if (lightSensor!=null){
            val timer = object: CountDownTimer(16000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    //Toast.makeText(applicationContext,"range max is "+lightSensor!!.maximumRange.toString()+"\n ${lightSensor!!.vendor}",Toast.LENGTH_SHORT).show()
                }
                override fun onFinish() {
                    // do something
                }
            }
            timer.start()
        }
    }

    private fun showWarningNoSensor() {
        Toast.makeText(applicationContext, "Your Phone not have light sensor",Toast.LENGTH_LONG).show()
        var alertDialog : AlertDialog = AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.light_sensor__error_title)+" \uD83D\uDE22")
            .setMessage(getString(R.string.light_sensor__error_message))
            .setNeutralButton("Okay:(",DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .show()
        val mp = MediaPlayer.create(this,R.raw.error)
        mp.start()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val millibarsOfPressure = event.values[0]
        // Do something with this sensor data.
        viewModel.lighter.value = millibarsOfPressure
        Log.d("xsensor"," $millibarsOfPressure")
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(this@LightSensorActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}