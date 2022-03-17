package com.example.bullrun.ui.fragments.coinInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.databinding.FragmentCoinInfoBinding

class CoinInfoFragment : Fragment() {
    lateinit var binding: FragmentCoinInfoBinding
    lateinit var viewModel: CoinInfoViewModel

    private val args: CoinInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            CoinInfoViewModelFactory(application, args.coin.coinId)
        ).get(CoinInfoViewModel::class.java)

        binding = FragmentCoinInfoBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.coinInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.coinInfo = viewModel.coinInfo.value
            }
        }

        binding.btnAddTransaction.setOnClickListener {
            val coinInfo = viewModel.getSmallCoinInfo()
            coinInfo?.let {
                navigateToAddTransactionPage(
                    it
                )
            }
        }
    }

    private fun navigateToAddTransactionPage(coin: Coin) {
        findNavController().navigate(
            CoinInfoFragmentDirections.actionCoinInfoFragmentToTransactionFragment(
                coin = coin
            )
        )
    }

}