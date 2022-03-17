package com.example.bullrun.ui.fragments.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bullrun.databinding.FragmentTransactionBinding
import com.example.bullrun.ui.fragments.coinInfo.CoinInfoFragmentArgs

class TransactionFragment : Fragment() {

    lateinit var binding: FragmentTransactionBinding
    lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            TransactionViewModelFactory(application)
        ).get(TransactionViewModel::class.java)

        binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    private val args: CoinInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.coinId.value = args.coin.coinId

        viewModel.coin.observe(viewLifecycleOwner) {
            if (it != null)
                binding.coin = viewModel.coin.value
        }

        binding.btnBuy.setOnClickListener {
            viewModel.buyAsset(
                binding.edtBuyingPrice.text.toString().toDouble(),
                binding.edtBuyingAmount.text.toString().toDouble(),
                binding.edtWalletName.text.toString()
            )
        }

        binding.btnSell.setOnClickListener {
            viewModel.sellAsset(
                binding.edtBuyingPrice.text.toString().toDouble(),
                binding.edtBuyingAmount.text.toString().toDouble(),
                binding.edtWalletName.text.toString()
            )
        }
    }

}