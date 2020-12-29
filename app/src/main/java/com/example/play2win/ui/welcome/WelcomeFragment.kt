package com.example.play2win.ui.welcome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.play2win.ProfileInfo
import com.example.play2win.R
import com.example.play2win.databinding.FragmentWelcomeBinding
import com.mindorks.retrofit.coroutines.ui.main.viewmodel.LoadWalletViewModel
import com.mindorks.retrofit.coroutines.utils.Status
import kotlinx.android.synthetic.main.fragment_welcome.progressBar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val navGraphScopedViewModel by navGraphViewModels<LoadWalletViewModel>(R.id.navigation)

    //private lateinit var viewModel: LoadWalletViewModel


    lateinit var  binding: FragmentWelcomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_welcome, container, false)

        binding.viewModel = navGraphScopedViewModel


        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.btnWin100.setOnClickListener {
            if (navGraphScopedViewModel.isLoggedIn()) {
                navGraphScopedViewModel.getProfile()?.let { it1 -> makeTransactionFive(it1) }
            } else {
                Toast.makeText(activity, "Load Account first!", Toast.LENGTH_LONG).show()
            }

        }

        binding.btnWin200.setOnClickListener (
          //findNavController().navigate(R.id.gameFragment)
          Navigation.createNavigateOnClickListener(R.id.action_welcomeFragment_to_gameFragment)
            )

        binding.tvLoadReload.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        observeViewModel()

        return binding.root
    }



    fun observeViewModel(){
        navGraphScopedViewModel.refreshBalance.observe(viewLifecycleOwner, Observer {
            loadAccount()
        })
    }


    private fun loadAccount() {
        navGraphScopedViewModel.getBalance().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                Log.d("DEBUG", resource.status.toString())
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        resource.data?.let { seed -> Log.d("DEBUG", seed.toString())
                            navGraphScopedViewModel.saveProfile( seed.balance)
                        }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, "Invalid account information!", Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }



    private fun makeTransactionFive(profileInfo: ProfileInfo){
        navGraphScopedViewModel.makeTransactionFive(profileInfo.account, profileInfo.pk).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                Log.d("DEBUG", resource.status.toString())
                when (resource.status) {
                    Status.SUCCESS -> {
                        //recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        Log.d("DEBUG", "its here")
                        //navGraphScopedViewModel.changeValue(false)
                        //Navigation.createNavigateOnClickListener(R.id.action_welcomeFragment_to_gameFragment)
                        view?.findNavController()?.navigate(

                                WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment()
                        )
                        resource.data?.let { seed -> Log.d("DEBUG", seed.toString())

                        }
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








    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WelcomeFragment()
    }
}