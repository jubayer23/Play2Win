/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.play2win.ProfileInfo
import com.example.play2win.R
import com.example.play2win.databinding.FragmentGameOverBinding
import com.mindorks.retrofit.coroutines.ui.main.viewmodel.LoadWalletViewModel
import com.mindorks.retrofit.coroutines.utils.Status
import kotlinx.android.synthetic.main.fragment_game_over.*

class GameOverFragment : Fragment() {
    private val navGraphScopedViewModel by navGraphViewModels<LoadWalletViewModel>(R.id.navigation)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameOverBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_over, container, false)
        binding.tryAgainButton.setOnClickListener {

            if (navGraphScopedViewModel.isLoggedIn()) {
                navGraphScopedViewModel.getProfile()?.let { it1 -> makeTransactionFive(it1) }
            } else {
                Toast.makeText(activity, "Load Account first!", Toast.LENGTH_LONG).show()
            }


        }

        binding.homeButton.setOnClickListener { findNavController().navigate(GameOverFragmentDirections.actionGameOverFragment2ToWelcomeFragment())
        }
        return binding.root
    }


    private fun makeTransactionFive(profileInfo: ProfileInfo){
        navGraphScopedViewModel.makeTransactionFive(profileInfo.account, profileInfo.pk).observe(this, Observer {
            it?.let { resource ->
                Log.d("DEBUG", resource.status.toString())
                when (resource.status) {
                    Status.SUCCESS -> {
                        //recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        Log.d("DEBUG", "its here")
                        //navGraphScopedViewModel.changeValue(false)
                        //Navigation.createNavigateOnClickListener(R.id.action_welcomeFragment_to_gameFragment)
                        findNavController().navigate(GameOverFragmentDirections.actionGameOverFragment2ToGameFragment())

                    }
                    Status.ERROR -> {
                        //recyclerView.visibility = View.VISIBLE
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
