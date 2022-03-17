package com.example.bullrun.ui.activitycoininfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.bullrun.R
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.databinding.FragmentCoinInfoBinding
import com.example.bullrun.ui.fragments.coinInfo.CoinInfoFragmentArgs
import com.example.bullrun.ui.fragments.coinInfo.CoinInfoFragmentDirections
import com.example.bullrun.ui.fragments.coinInfo.CoinInfoViewModelFactory

class CoinInfoActivity : AppCompatActivity() {

    lateinit var binding: FragmentCoinInfoBinding
    lateinit var viewModel: CoinInfoViewModel

    private val args: CoinInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_coin_info
        )

        val application = requireNotNull(this).application
        viewModel = ViewModelProvider(
            this,
            CoinInfoViewModelFactory(application, args.coin.coinId)
        ).get(CoinInfoViewModel::class.java)



        viewModel.coinInfo.observe(this) {
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
        this.findNavController(binding.root.id).navigate(
            CoinInfoFragmentDirections.actionCoinInfoFragmentToTransactionFragment(
                coin = coin
            )
        )
    }
}