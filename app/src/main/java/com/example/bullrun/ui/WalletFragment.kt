package com.example.bullrun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bullrun.databinding.FragmentWalletBinding


class WalletFragment : Fragment() {

    lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val displayMetrics = resources.displayMetrics
//        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
//        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
//        binding.l1.layoutParams.width=1000
//        binding.l1.layoutParams.height=300
//        binding.l2.layoutParams.width=1000
//        binding.l2.layoutParams.height=300
    }


}