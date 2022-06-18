package com.revolve44.solarpanelx.ui.fragments.features

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.revolve44.solarpanelx.BuildConfig
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.domain.core.gradientAnimation

//source of inspiration: https://en.wikipedia.org/wiki/List_of_rivers_by_length
class AboutFragment : Fragment(R.layout.fragment_about) {

    private lateinit var shieldik : TextView
    private lateinit var backof_aboutscreen : ConstraintLayout
    private lateinit var revolnaGoSite : TextView
    private lateinit var twiGoSite : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shieldik = view.findViewById(R.id.shieldik)
        backof_aboutscreen = view.findViewById(R.id.backof_aboutscreen)
        revolnaGoSite = view.findViewById(R.id.textView12)
        twiGoSite = view.findViewById(R.id.twi_go_site)

        shieldik.text = "Solar Panel X\n ver.${BuildConfig.VERSION_NAME}"


        gradientAnimation(backof_aboutscreen,Color.WHITE,Color.BLUE,Color.MAGENTA,Color.YELLOW,Color.WHITE,7000)
        backof_aboutscreen.setOnClickListener {
            gradientAnimation(backof_aboutscreen,Color.WHITE,Color.BLUE,Color.MAGENTA,Color.YELLOW,Color.WHITE,20000)
        }

        twiGoSite.setOnClickListener {
            goToUrl("https://twitter.com/arstagaev")
        }
        revolnaGoSite.setOnClickListener {
            goToUrl("http://revolna.com/")
        }


    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }
}