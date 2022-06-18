package com.revolve44.solarpanelx.feature_modules.finance

import com.revolve44.solarpanelx.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.revolve44.solarpanelx.feature_modules.finance.models.SavingEconomyItem
import com.revolve44.solarpanelx.feature_modules.finance.ui.theme.SolarPanelXTheme

class FinanceActivity : ComponentActivity() {

    var savingEconomyItems = mutableStateListOf<SavingEconomyItem>(
        SavingEconomyItem("Reducing Electric Bills"                ,0,0),
        SavingEconomyItem("Outage Protection"                      ,0,0),
        SavingEconomyItem("Investing savings in stocks [Yield: 8%]",0,0),
        SavingEconomyItem("Energy Independence"                    ,0,0),
    )

    var savingEconomyItems2 = mutableStateListOf<SavingEconomyItem>(
        SavingEconomyItem("Cost Of PV System",0,0),

    )
    var fontMontserrat : FontFamily? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fontMontserrat = FontFamily(ResourcesCompat.getFont(applicationContext,R.font.montserrat)!!)
        setContent {
            SolarPanelXTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
//                        Box(
//                            Modifier.fillMaxWidth().height(200.dp)) {
//
//                        }
                        Text(
                            text = "My Capital (Cost of PV Station + Savings):",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .background(Color.Red)
                                .padding(3.dp)
                                //.align(Alignment.TopCenter)
                            ,
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,fontFamily = fontMontserrat
                        )
                        Text(
                            text = "1997 $",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(3.dp)
                                .background(Color.Cyan)

                                //.align(Alignment.BottomCenter)
                            ,
                            textAlign = TextAlign.Center,
                            fontSize = 65.sp,fontFamily = fontMontserrat
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 20.dp, vertical = 5.dp)
                                .clickable { }
                                .shadow(elevation = 20.dp, shape = RoundedCornerShape(12.dp)),
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.DarkGray)) {
                                Text(
                                    text = "+23$ (+0.25%)",
                                    color = Color.Green,
                                    modifier = Modifier
                                        .width(IntrinsicSize.Max)
                                        .height(IntrinsicSize.Min)
                                        .padding(horizontal = 15.dp)
                                        .align(Alignment.CenterStart),textAlign = TextAlign.Center,
                                    fontSize = 15.sp,fontFamily = fontMontserrat
                                )
                                Divider(
                                    color = Color.LightGray,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(1.dp)
                                        .padding(vertical = 9.dp)
                                        .align(Alignment.Center)
                                )
                                Text(
                                    text = "savings per year",
                                    color = colorResource(R.color.day_sun_zenith),
                                    modifier = Modifier
                                        .width(IntrinsicSize.Max)
                                        .height(IntrinsicSize.Min)
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 15.dp),textAlign = TextAlign.Center,
                                    fontSize = 15.sp,fontFamily = fontMontserrat
                                )
                            }
                        }

                        LazyColumn (
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .fillMaxHeight()
                            ,
                        ) {
                            items(savingEconomyItems.toList()) { sav ->
                                savingRow(sav)
                            }
                        }


                    }
                }
            }
        }
    }
    @Composable
    private fun savingRow(sav: SavingEconomyItem) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(Color.Black)
                .clickable {  }
        ) {
            Text(
                text = "${sav.nameOfSaving} \n${sav.countSaved}",
                color = colorResource(R.color.hint_white),
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(4.dp),
                textAlign = TextAlign.Start,
                fontSize = 25.sp,
                fontFamily = fontMontserrat
            )
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

    }
}

