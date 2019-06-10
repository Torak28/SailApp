package com.pwr.sailapp.ui.generic

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pwr.sailapp.ui.login.INVALID_CREDENTIALS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment : Fragment(), CoroutineScope{
    private lateinit var job:Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun toast(text: String) = Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()

    protected open fun changeLoadingBarVisibility(isVisible: Boolean){}

    protected fun snack(msg: String){
        Snackbar.make(
            view!!,
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}