package com.pwr.sailapp.ui.generic

import com.pwr.sailapp.data.DataProvider
import com.pwr.sailapp.ui.generic.ScopedFragment
import com.pwr.sailapp.viewModel.LoginViewModel
import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel

abstract class LoginScopedFragment : ScopedFragment() {
    private val dataProvider by lazy {
        DataProvider.getInstance(requireActivity().applicationContext)
    }

    protected val loginViewModel by lazy{
        requireActivity().getViewModel {
            LoginViewModel(
                requireActivity().application,
                dataProvider.userManager
            )
        }
    }

}