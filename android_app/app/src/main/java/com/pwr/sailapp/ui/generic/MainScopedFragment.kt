package com.pwr.sailapp.ui.generic

import com.pwr.sailapp.data.DataProvider
import com.pwr.sailapp.ui.generic.ScopedFragment
import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel

abstract class MainScopedFragment : ScopedFragment() {
    protected val dataProvider by lazy {
        DataProvider.getInstance(requireActivity().applicationContext)
    }

    protected val mainViewModel by lazy{
        getViewModel {
            MainViewModel(
                requireActivity().application,
                dataProvider.repository,
                dataProvider.userManager
            )
        }
    }

}