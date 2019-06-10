package com.pwr.sailapp.ui.generic

import com.pwr.sailapp.viewModel.LoginViewModel
import com.pwr.sailapp.viewModel.getViewModel

abstract class LoginScopedFragment : ScopedFragment() {

    protected val loginViewModel by lazy{
        requireActivity().getViewModel {
            LoginViewModel(
                requireActivity().application
            )
        }
    }

}