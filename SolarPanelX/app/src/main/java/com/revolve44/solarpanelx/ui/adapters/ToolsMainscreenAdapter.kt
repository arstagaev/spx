package com.revolve44.solarpanelx.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.domain.base.recyclerview.BaseAdapter
import com.revolve44.solarpanelx.domain.base.recyclerview.BaseViewHolder
import com.revolve44.solarpanelx.domain.base.recyclerview.ItemElementsDelegate
import com.revolve44.solarpanelx.ui.models.ToolsRecyclerviewModel


class ToolsMainscreenAdapter: BaseAdapter<ToolsRecyclerviewModel>(){

    var pairDelegate: ItemElementsDelegate<ToolsRecyclerviewModel>? = null

    fun attachDelegate(callback: ItemElementsDelegate<ToolsRecyclerviewModel>) {
        this.pairDelegate = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ToolsRecyclerviewModel> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tools_screen,parent,false))

    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<ToolsRecyclerviewModel>(itemView = itemView){
        private val txtName : TextView = itemView.findViewById(R.id.name_in_row_recycler_tools)
        private val iconTool : ImageView = itemView.findViewById(R.id.icon_of_tools)
        //private val rssiLevel : TextView = itemView.findViewById(R.id.signal_strength)
        //private val bondStatus : TextView = itemView.findViewById(R.id.bond_status)

        private val cardLayout : CardView = itemView.findViewById(R.id.card_row_recycler_tools)


        override fun bind(model: ToolsRecyclerviewModel) {
            txtName.text = model.name
            iconTool.setImageResource(model.iconOfTool)

            //macAdrress.text = model.device.address
            //rssiLevel.text = model.rssi.toString()+" dBm"
            //bondStatus.text = bondStateConverter(model.device.bondState)
            cardLayout.setOnClickListener {

                pairDelegate?.onElementClick(model,itemView,this.adapterPosition)

            }
        }
    }
}