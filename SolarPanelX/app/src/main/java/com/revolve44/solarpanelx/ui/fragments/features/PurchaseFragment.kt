package com.revolve44.solarpanelx.ui.fragments.features

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.SkuDetails
import com.anjlab.android.iab.v3.TransactionDetails
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import timber.log.Timber
import java.util.*


class PurchaseFragment : Fragment(R.layout.fragment_purchace) , BillingProcessor.IBillingHandler,
    PurchasesUpdatedListener {

    private var bp: BillingProcessor? = null
    private var purchaseTransactionDetails : TransactionDetails? = null
    private var purchaseSkuDetails         : SkuDetails? = null

    private lateinit var purchase_screen_button_make_purchase : MaterialButton
    private lateinit var tvStatus : TextView
    private val PRODUCT_ID = "annually_subscription1"
    private var billingClient: BillingClient? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        purchase_screen_button_make_purchase = view.findViewById(R.id.purchase_screen_button_make_purchase)
        tvStatus = view.findViewById(R.id.purchase_screen_current_plan_viewer)

        //init billing
        bp = BillingProcessor(requireActivity(), PRODUCT_ID, this)
        bp!!.initialize()
        setupBillingClient()
        //bp!!.isSubscriptionUpdateSupported()
        if(!BillingProcessor.isIabServiceAvailable(requireActivity())) {
            Toast.makeText(requireActivity(),"In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16",Toast.LENGTH_LONG).show();
        }
    }

    private fun hasSubscription(): Boolean {
        return if (purchaseSkuDetails != null) {
            purchaseSkuDetails!!.description != null
        } else false
    }



    override fun onBillingInitialized() {

        //purchaseTransactionDetails = bp!!.getSubscriptionTransactionDetails(PRODUCT_ID)
        purchaseSkuDetails = bp!!.getSubscriptionListingDetails(PRODUCT_ID)
        bp!!.loadOwnedPurchasesFromGoogle()

        purchase_screen_button_make_purchase.setOnClickListener(View.OnClickListener { v: View? ->
//            goToUrl("https://play.google.com/store/apps/details?id=com.revolve44.solarpanelxpro")
//            Toast.makeText(requireActivity(),"In this week we create payment method",Toast.LENGTH_LONG).show()
            if (bp!!.isSubscriptionUpdateSupported) {

                bp!!.subscribe(requireActivity(), PRODUCT_ID)

            } else {
                Timber.w("bbb onBillingInitialized: Subscription updated is not supported")
            }
        })


    }

    fun premiumOrNotInspector(){

        if (PreferenceMaestro.isPremiumStatus) {
            tvStatus.setText(getString(R.string.current_plan_premium))
           // PreferenceMaestro.isPremiumStatus = true
        }else{
            tvStatus.setText(getString(R.string.current_plan_free))
           // PreferenceMaestro.isPremiumStatus = false
        }

        Timber.w("bbb ${PreferenceMaestro.isPremiumStatus} ~  ${bp?.isSubscribed(PRODUCT_ID)} ")
    }

    override fun onResume() {
        super.onResume()
        isUserHasSubscription(requireActivity())

        premiumOrNotInspector()
        Timber.w("")


    }



    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        Timber.w("bbb onProductPurchased ${productId} ${details?.purchaseInfo}")
        premiumOrNotInspector()
        Snackbar.make(requireActivity().findViewById(android.R.id.content),"Congratulations on your purchase!"+"✨", Snackbar.LENGTH_LONG).show()

    }

    override fun onPurchaseHistoryRestored() {
        Timber.w("bbb onPurchaseHistoryRestored")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Timber.e("bbb onBillingError ${error?.message}")
        Snackbar.make(requireActivity().findViewById(android.R.id.content),"Error: ${error?.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        if (bp != null) {
            bp!!.release()
        }
        super.onDestroy()
    }

    private fun isUserHasSubscription(context: Context)  {
        var billingClient =
            BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val purchasesResult: PurchasesResult =
                    billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) { billingResult1, list ->
                    Timber.d("billingprocess ::: purchasesResult.getPurchasesList():" + purchasesResult.purchasesList)
                    if (billingResult1.getResponseCode() === BillingClient.BillingResponseCode.OK &&
                        !Objects.requireNonNull(purchasesResult.purchasesList).isEmpty()
                    ) {
                        Timber.w("bbb Subs active!!")
                        //here you can pass the user to use the app because he has an active subscription
                        PreferenceMaestro.isPremiumStatus = true
                    }else{
                        Timber.w("bbb Subs NOT active!!")
                        PreferenceMaestro.isPremiumStatus = false
                    }
                    premiumOrNotInspector()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("billingprocess", "onBillingServiceDisconnected")
            }
        })
    }


    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        Timber.w("pizdec onpurchaseUPD ${p0?.responseCode} ${p1?.joinToString()} ")
    }

    fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(requireActivity()!!)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        //startConnection()
    }



}