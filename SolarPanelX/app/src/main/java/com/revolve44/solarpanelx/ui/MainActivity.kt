package com.revolve44.solarpanelx.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.ui.fragments.MainScreenFragment
import com.revolve44.solarpanelx.ui.fragments.MainScreenFragmentDirections
import com.revolve44.solarpanelx.ui.fragments.ToolsManagerFragment
import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel
import com.revolve44.solarpanelx.ui.viewmodels.MassiveViewModelProviderFactory


class MainActivity : AppCompatActivity() {
    //private lateinit var navController: NavController
    private lateinit var iconMainScreenBottomNavigation : ImageView
    private lateinit var iconToolsBottomNavigation      : ImageView

    private lateinit var bottomNavView : BottomNavigationView
    var navController: NavController? = null

    //var viewModelMain : MainViewModel? = null
    //private val viewModelMain : MainViewModel by viewModels()
    //val viewModelMain: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val spxRepository = SpxRepository(application)

        //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)
        //viewModelMain = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)

        bottomNavView = findViewById(R.id.bottom_nav)

        navController = Navigation.findNavController(this, R.id.main_screen_container_fragment);

        bottomNavView.setupWithNavController(navController!!)
        setCurrentFragment()
        manageBottomNavBar()
        //val navHostFragment =
        //    supportFragmentManager.findFragmentById(R.id.main_screen_container_fragment) as NavHostFragment
        //navController = navHostFragment.navController
        //navController.navigate(R.id.action_mainFragment_to_toolsManagerFragment)


        //iconMainScreenBottomNavigation = findViewById(R.id.row_home_mainscreen)
        //iconToolsBottomNavigation      = findViewById(R.id.row_manager_of_tools)


                //val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_screen_container_fragment) as NavHostFragment
        //val navController = navHostFragment.navController

        //navController = findNavController(R.id.main_screen_container_fragment)
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
                    navController?.navigate(R.id.mainFragment)
                    //findNavController(R.id.main_screen_container_fragment).navigate(MainScreenFragmentDirections.actionMainFragmentToToolsManagerFragment())

                }
                R.id.ToolsScreen -> {
                    navController?.navigate(R.id.toolsManagerFragment)
                    //findNavController(R.id.main_screen_container_fragment).navigate(ToolsManagerFragmentDirections.actionToolsManagerFragmentToMainFragment())

                }
            }
            true
        }

    }

    private fun setCurrentFragment() {
        //supportFragmentManager.beginTransaction().apply {
        //    replace(R.id.con)
        //}
    }

    private var isOpeniconMainScreenBottomNavigation = true
    private fun initBottomNav() {
        if (isOpeniconMainScreenBottomNavigation){
            iconMainScreenBottomNavigation.clearColorFilter()
            iconToolsBottomNavigation.setColorFilter(Color.GRAY);
        }else{
            iconMainScreenBottomNavigation.setColorFilter(Color.GRAY);
            iconToolsBottomNavigation.clearColorFilter()
        }
        // Main Screen
        iconMainScreenBottomNavigation.setOnClickListener {
            isOpeniconMainScreenBottomNavigation = true
            iconMainScreenBottomNavigation.clearColorFilter()
            iconToolsBottomNavigation.setColorFilter(Color.GRAY);

        }
        // Tools Screen
        iconToolsBottomNavigation.setOnClickListener {
            isOpeniconMainScreenBottomNavigation = false
            iconMainScreenBottomNavigation.setColorFilter(Color.GRAY);
            iconToolsBottomNavigation.clearColorFilter()

            findNavController(R.id.main_screen_container_fragment).navigate(MainScreenFragmentDirections.actionMainFragmentToToolsManagerFragment())

        }
    }
}