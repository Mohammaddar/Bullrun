package com.example.bullrun.ui.fragments.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bullrun.databinding.FragmentPortfolioBinding
import com.example.bullrun.ui.MainActivity
import kotlinx.coroutines.launch
import kotlin.random.Random


class PortfolioFragment : Fragment() {

    lateinit var binding: FragmentPortfolioBinding
    lateinit var viewModel: PortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            PortfolioViewModelFactory(application)
        ).get(PortfolioViewModel::class.java)

        binding = FragmentPortfolioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).viewModel.bottomNavigationState.value="VISIBLE"

        setupRecyclerWallets()

        binding.btnNewWallet.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addWallet("Wallet ${Random.nextInt(0, 100)}")
            }
        }

        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////


//        val ls = mutableListOf<AssetUI>()
//        repeat(30) {
//            ls.add(
//                AssetUI(
//                    "btc",
//                    "vis",
//                    "",
//                    "",
//                    1522.21,
//                    2.2,
//                    12.5,
//                    4863.5,
//                    4832.6
//                )
//            )
//        }
//        adapter.submitList(
//            ls as List<AssetItem>?
//        )


//        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
//            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
//            binding.motionLayout.progress = if (seekPosition.isNaN()) 0f else seekPosition
//            Log.d("TAGM", "$verticalOffset")
//            Log.d("TAGM", "${binding.appbarLayout.totalScrollRange.toFloat()}")
//            Log.d("TAGM", "$seekPosition")
//        }
//        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }

    private fun setupRecyclerWallets() {
        val walletAdapter = WalletListAdapter(requireNotNull(context)) {
            findNavController().navigate(
                PortfolioFragmentDirections.actionPortfolioFragment2ToWalletActivity(
                    it.walletName
                )
            )
            //TODO
        }
        binding.recyclerWallets.adapter = walletAdapter
        binding.recyclerWallets.layoutManager = LinearLayoutManager(activity)
        viewModel.walletList.observe(viewLifecycleOwner) {
            walletAdapter.submitList(it)
        }
    }


//    private fun setupBottomSheet() {
//        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
//        standardBottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
//        standardBottomSheetBehavior.run {
//            state = BottomSheetBehavior.STATE_HIDDEN
//            peekHeight = screenHeight - binding.appBar.layoutParams.height - 32
//            binding.btnWalletMenu.setOnClickListener {
//                if (state == BottomSheetBehavior.STATE_HIDDEN) {
//                    state = BottomSheetBehavior.STATE_COLLAPSED
//                    isHideable = false
//                } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
//                    isHideable = true
//                    state = BottomSheetBehavior.STATE_HIDDEN
//                    isHideable = false
//                }
//            }
//
//            val appBarLayoutParams: AppBarLayout.LayoutParams =
//                binding.motionLayout.layoutParams as AppBarLayout.LayoutParams
//
//            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//
//                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                        isDraggable = false
//                        if (appBarLayoutParams.scrollFlags == 0) {
//                            appBarLayoutParams.scrollFlags =
//                                (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
//                                        or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)
//                            binding.motionLayout.layoutParams = appBarLayoutParams
//                            binding.appbarLayout.isEnabled = true
//                        }
//
//                    } else {
//                        isDraggable = true
//                        if (appBarLayoutParams.scrollFlags != 0) {
//                            appBarLayoutParams.scrollFlags = 0
//                            binding.motionLayout.layoutParams = appBarLayoutParams
//                            binding.appbarLayout.isEnabled = false
//                        }
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    binding.motionLayout.progress = if (slideOffset < 0f) 0f else slideOffset
//                }
//
//            })
//
//            binding.appbarLayout.setOnClickListener {
//                if (state == BottomSheetBehavior.STATE_EXPANDED)
//                    state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//            binding.appbarLayout.isEnabled = false
//        }
//
//    }


}



