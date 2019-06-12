package com.pwr.sailapp.ui.generic

import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel

abstract class MainScopedFragment : ScopedFragment() {

    protected val mainViewModel by lazy{
        requireActivity().getViewModel { MainViewModel(requireActivity().application) }
    }
}