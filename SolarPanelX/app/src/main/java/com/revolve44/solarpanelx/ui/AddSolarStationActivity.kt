package com.revolve44.solarpanelx.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.setLocale
import com.revolve44.solarpanelx.ui.fragments.createstation.LastConfirmFragment
import com.revolve44.solarpanelx.ui.fragments.createstation.MapFragment
import kotlinx.android.synthetic.main.activity_add_station.*
import timber.log.Timber

class AddSolarStationActivity : AppCompatActivity() {

    private lateinit var viewPager2 : ViewPager2
    private var mainActivity : MainActivity = MainActivity()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_station)
        Timber.i("AddSolarStationActivity started")

        setLocale(this, PreferenceMaestro.languageOfApp)


//        val repo = SolarRepository(application)
//
//        val viewModelAddSolarStation: ViewModelAddSolarStation =
//                ViewModelProvider(this).get(ViewModelAddSolarStation::class.java)



//        toCharacteristics = findViewById(R.id.to_characteristics)
        viewPager2 = findViewById(R.id.viewPager2)

        //viewModelAddSolarStation.addSolarStation(SolarStation(12,144,"HORRAYY!!"))




        viewPager2.adapter = ViewPagerFragmentAdapter(this)
        //disabling swipe in viewpager
        viewPager2.isUserInputEnabled = false
        val titles = arrayOf(getString(R.string.addstation_activity_map),getString(R.string.addstation_activity_characteristics))


        // attaching tab mediator
        TabLayoutMediator(tab_layout, viewPager2) { tab: TabLayout.Tab, position: Int ->
            tab.setText(titles.get(position))
        }.attach()
        //to_characteristics.setOnClickListener { }

    }

    fun gotoSecondPage() {
        viewPager2.currentItem = 2
    }

    class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val titles = arrayOf(fragmentActivity.getString(R.string.addstation_activity_map),fragmentActivity.getString(R.string.addstation_activity_characteristics))

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return MapFragment()
                1 -> return LastConfirmFragment()
                //2 -> return TicketsFragment()
            }
            return MapFragment()
        }

        override fun getItemCount(): Int {
            return titles.size
        }
    }

    override fun onBackPressed() {
        if (PreferenceMaestro.isFirstStart) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please, fill correct all forms",
                Snackbar.LENGTH_LONG
            ).show()
        }else{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("changingdata",false)
            super.onBackPressed()
        }

    }
}