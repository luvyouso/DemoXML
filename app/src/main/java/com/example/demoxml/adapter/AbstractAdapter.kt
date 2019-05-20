package com.example.demoxml.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListItemInteractionListener {

        fun onInteraction(view: View, model: Any, position: Int)
    }
}