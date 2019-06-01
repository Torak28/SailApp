package com.pwr.sailapp.ui.generic

import androidx.lifecycle.ViewModelProviders
import com.pwr.sailapp.data.DataProvider
import com.pwr.sailapp.ui.generic.ScopedFragment
import com.pwr.sailapp.viewModel.BaseViewModelFactory
import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel

abstract class MainScopedFragment : ScopedFragment() {

    protected val mainViewModel by lazy{
        requireActivity().getViewModel { MainViewModel(requireActivity().application) }
    }
}