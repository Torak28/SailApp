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
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 */
class CruiseFragment : Fragment() {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var maxLight: Float? = null

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
        Log.d("Sensor max", maxLight.toString())
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(lightListener)
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
    private fun adjustLightValue(lightValue: Float) = (100 * (-exp(-0.001 * lightValue) + 1)).toInt()

}
