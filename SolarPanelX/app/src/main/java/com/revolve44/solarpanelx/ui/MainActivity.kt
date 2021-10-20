package com.revolve44.solarpanelx.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.setLocale
import com.revolve44.solarpanelx.feature_modules.workmanager.model.NotificationWarningModel
import com.revolve44.solarpanelx.ui.fragments.MainScreenFragment
import com.revolve44.solarpanelx.ui.fragments.MainScreenFragmentDirections
import com.revolve44.solarpanelx.ui.fragments.ToolsManagerFragment
import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel
import com.revolve44.solarpanelx.ui.viewmodels.MassiveViewModelProviderFactory
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    //private lateinit var navController: NavController
    private lateinit var iconMainScreenBottomNavigation : ImageView
    private lateinit var iconToolsBottomNavigation      : ImageView

    private lateinit var bottomNavView : BottomNavigationView
    var navController: NavController? = null

    //var viewModelMain : MainViewModel? = null
    //private val viewModelMain : MainViewModel by viewModels()
    var viewModelMain: MainViewModel? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // if not installed pv station => go to addStationActivity
        setLocale(this,PreferenceMaestro.languageOfApp)
        firstLaunch()

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_main)


        var spxRepository = SpxRepository(application)

        val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)
        viewModelMain = ViewModelProvider(this@MainActivity,viewModelFactory).get(MainViewModel::class.java)

        // get from addstation request to update forecast (below)
        val intent = intent
        if (intent.getBooleanExtra("changingdata",false)) {

            PreferenceMaestro.timeOfLastDataUpdateLong = 0
            viewModelMain?.manualRequest()

        }

        bottomNavView = findViewById(R.id.bottom_nav)

        initNavigation()
        manageBottomNavBar()
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(this, R.id.main_screen_container_fragment)
        val navGraph = navController!!.navInflater.inflate(R.navigation.nav_main_screen)
        //navController!!.setGraph(R.navigation.)

        if (!PreferenceMaestro.isPremiumStatus){
            when((0..10).random()){
                0,1 -> {
                    navGraph.startDestination = R.id.purchaseFragment;
                }
                else ->{
                    navGraph.startDestination = R.id.mainFragmentOfApp;
                }
            }


        }

        navController!!.graph = navGraph;

        bottomNavView.setupWithNavController(navController!!)
    }

    private fun firstLaunch() {
        // will set low animation preferences if is old device. I think old device is API with version under 10 API
        // API 28 = Android 9
        if (Build.VERSION.SDK_INT <= 28){
            PreferenceMaestro.isLightMode = true
        }

        if (PreferenceMaestro.isFirstStart) {

            val intent = Intent(this, AddSolarStationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        //.initBottomNav()

    }

    private fun manageBottomNavBar() {
        val firstFragment = MainScreenFragment()
        val secFragment = ToolsManagerFragment()
        bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.MainScreenForecast -> {
                    navController?.navigate(R.id.mainFragmentOfApp)
                    //findNavController(R.id.main_screen_container_fragment).navigate(MainScreenFragmentDirections.actionMainFragmentToToolsManagerFragment())

                }
                R.id.ToolsScreen -> {
                    navController?.navigate(R.id.tlMng)
                    //findNavController(R.id.main_screen_container_fragment).navigate(ToolsManagerFragmentDirections.actionToolsManagerFragmentToMainFragment())

                }

                R.id.PurchaseScreen -> {
                    navController?.navigate(R.id.purchaseFragment)
                    //findNavController(R.id.main_screen_container_fragment).navigate(ToolsManagerFragmentDirections.actionToolsManagerFragmentToMainFragment())

                }
            }
            true
        }
    }
}
