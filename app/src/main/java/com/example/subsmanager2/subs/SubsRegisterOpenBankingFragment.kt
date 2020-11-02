package com.example.subsmanager2.subs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import kotlinx.android.synthetic.main.fragment_subs_register_open_banking.view.*

/**
 * A simple [Fragment] subclass.
 */
class SubsRegisterOpenBankingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_subs_register_open_banking, container, false)

        rootView.btnAuthToken.setOnClickListener {
            findNavController().navigate(R.id.action_subsRegisterOpenBankingFragment_to_centerAuthFragment)
        }

        rootView.btnAPICallMenu.setOnClickListener {
            findNavController().navigate(R.id.action_subsRegisterOpenBankingFragment_to_centerAuthAPIFragment)
        }

        return rootView
    }

}
