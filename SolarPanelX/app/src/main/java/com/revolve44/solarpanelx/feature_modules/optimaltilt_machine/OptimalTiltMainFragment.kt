package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel

import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps.*

class OptimalTiltMainFragment : Fragment(R.layout.sm_fragment_start_calc_tilt) {


    private lateinit var viewPager: ViewPager2
    private lateinit var buttonToNext : CardView
    private lateinit var viewModelOrientation : OrientationSolarPanelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = view.findViewById(R.id.pager_calc_tilt)
        //buttonToNext = view.findViewById(R.id.next_from_three_fragment)
        viewModelOrientation = (activity as OptimalTiltHelperActivity).viewModel
        viewModelOrientation.currentItemInViewPager2.value = viewPager.currentItem

        viewModelOrientation.currentItemInViewPager2.observe(viewLifecycleOwner, Observer {
            viewPager.currentItem = it
        })



        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        viewPager.setPageTransformer(ZoomOutPageTransformer())

        //buttonToNext.setOnClickListener {
        //    findNavController().navigate(R.id.action_mainFragment_to_secondFragment)
        //}

    }

    /**
     * A simple pager adapter that represents com.arstagaev.calcbalance.test.ScreenSlidePageFragment
     * objects, in sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {


        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CalibrateOrientationSensorFragment()
                1 -> CalcHelperFragment()
                2 -> LocationOfPhoneOnSPSurfaceFragment()
                3 -> AzimithDirectRotateSolarPanelFragment()
                4 -> TiltOfSolarPanel()
                else -> CalcHelperFragment()
            }
        }
    }

    companion object {
        /**
         * The number of pages (wizard steps) to show in this demo.
         */
        private const val NUM_PAGES = 5
    }
}