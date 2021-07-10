package com.example.newspx1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class AccelerometerActivity : AppCompatActivity() , SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var txt1 : TextView
    private lateinit var txt2 : TextView
    private lateinit var txt3 : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerometer)

        txt1 = findViewById(R.id.first)
        txt2 = findViewById(R.id.scnd2)
        txt3 = findViewById(R.id.thied3)

        //sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
        // Rotation matrix based on current readings from accelerometer and magnetometer.

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)

// Express the updated rotation matrix as three orientation angles.
        val orientationAngles = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)




    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event?.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event?.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }
        updateOrientationAngles()
       // Log.i("ggg","${Math.toDegrees(event!!.values[0].toDouble())} ${Math.toDegrees(event!!.values[1].toDouble())} ${Math.toDegrees(event!!.values[2].toDouble())}")
        Log.i("ccc"," acc read ${Math.toDegrees(accelerometerReading[0].toDouble())} ${Math.toDegrees(accelerometerReading[1].toDouble())}  ${Math.toDegrees(accelerometerReading[2].toDouble())}  // orient ang ${Math.toDegrees(orientationAngles[0].toDouble())} ${Math.toDegrees(orientationAngles[1].toDouble())} ${Math.toDegrees(orientationAngles[2].toDouble())}")
        txt1.text = "${Math.toDegrees(accelerometerReading[0].toDouble())}"
        txt2.text = "${Math.toDegrees(accelerometerReading[1].toDouble())}"
        txt3.text = "${Math.toDegrees(accelerometerReading[2].toDouble())}"
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // "orientationAngles" now has up-to-date information.
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this)
    }
}