/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.play2win.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.play2win.ProfileInfo
import com.example.play2win.R
import com.example.play2win.databinding.FragmentEditProfileBinding
import com.mindorks.retrofit.coroutines.ui.main.viewmodel.LoadWalletViewModel
import com.mindorks.retrofit.coroutines.utils.Status
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class AccountLoadDialog : DialogFragment() {

  private val navGraphScopedViewModel: LoadWalletViewModel by navGraphViewModels(R.id.navigation)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding: FragmentEditProfileBinding = DataBindingUtil.inflate(
      inflater, R.layout.fragment_edit_profile, container, false
    )
    binding.viewModel = navGraphScopedViewModel
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    dialog?.setOnShowListener {
      dialog?.setTitle("Load Account")
      dialog?.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      navGraphScopedViewModel.loadProfile()

    }

    dialog?.setOnDismissListener {
        navGraphScopedViewModel.clearFormError()
    }

    btnSave.setOnClickListener {
      if(navGraphScopedViewModel.isFormValid())
      loadAccount()
    }

  }


  private fun loadAccount() {
    navGraphScopedViewModel.getBalance().observe(this, Observer {
      it?.let { resource ->
        Log.d("DEBUG", resource.status.toString())
        when (resource.status) {
          Status.SUCCESS -> {
            progressBar.visibility = View.GONE
            resource.data?.let { seed -> Log.d("DEBUG", seed.toString())
              navGraphScopedViewModel.saveProfile( seed.balance)
              dismiss()
            }
          }
          Status.ERROR -> {
            Toast.makeText(activity, "Invalid account credentials", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
          }
          Status.LOADING -> {
            progressBar.visibility = View.VISIBLE
          }
        }
      }
    })
  }
}
