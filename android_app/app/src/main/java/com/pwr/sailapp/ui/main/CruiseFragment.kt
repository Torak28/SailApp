package com.pwr.sailapp.ui.main


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pwr.sailapp.R
import kotlinx.android.synthetic.main.fragment_cruise.*
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

/**
 * A simple [Fragment] subclass.
 */
class CruiseFragment : Fragment() {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var maxLight: Float? = null
    private var linearAccelerationSensor: Sensor? = null
    private var displayedAcceleration: FloatArray? = floatArrayOf(0f, 0f, 0f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cruise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        maxLight = lightSensor?.maximumRange
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        arrayOf(text_acc_x, text_acc_y, text_acc_z).map { it.text = "0" }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(
            linearAccelerationListener,
            linearAccelerationSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(lightListener)
        sensorManager.unregisterListener(linearAccelerationListener)
    }

    private val lightListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val lightValue = event?.values?.get(0)
            Log.d("Sensor", lightValue.toString())
            renderLightnessInfo(lightValue)
        }
    }

    private fun renderLightnessInfo(lightValue: Float?) {
        if (lightValue == null || maxLight == null) {
            text_lightness.text = resources.getString(R.string.no_light_sensor)
            progress_bar_lightness.visibility = View.INVISIBLE
        } else {
            val adjustedLight = adjustLightValue(lightValue)
            text_lightness.text = adjustedLight.toString()
            progress_bar_lightness.progress = adjustedLight
        }
    }

    /**
     * @param lightValue value read by lightness sensor
     * @return exponential mapped value (small values amplified)
     */
    private fun adjustLightValue(lightValue: Float) =
        (100 * (-exp(-0.001 * lightValue) + 1)).toInt()

    private val linearAccelerationListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            renderAccelerationInfo(event?.values)
        }

    }

    private fun renderAccelerationInfo(accelerationValues: FloatArray?) {
        val views = arrayOf(text_acc_x, text_acc_y, text_acc_z)
        if (displayedAcceleration != null && accelerationValues != null) {
            for ((index, view) in views.withIndex()) {
                val diffNoAbs = displayedAcceleration!![index] - accelerationValues[index]
                Log.d("accDiff", diffNoAbs.toString())
                val diff = abs(displayedAcceleration!![index] - accelerationValues[index])
                Log.d("accDiffABS", diff.toString())
                view.text = String.format("%.2f", accelerationValues[index])
            }
        } else {
            views.map { it.text = "?" }
        }
    }

}
