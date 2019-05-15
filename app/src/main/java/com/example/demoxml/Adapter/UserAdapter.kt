package com.example.demoxml.Adapter

import android.app.Notification
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demoxml.R
import com.example.demoxml.model.User
import kotlinx.android.synthetic.main.layout_item_user.view.*

class UserAdapter(private val mContext: Context, private var mListData: MutableList<User>?) : AbstractAdapter() {

    private var mItemInteractionListener: AbstractAdapter.ListItemInteractionListener? = null
    /**
     * @param listener
     */
    fun setItemInteractionListener(listener: AbstractAdapter.ListItemInteractionListener) {
        this.mItemInteractionListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_user, parent, false))
    }

    override fun getItemCount(): Int {
        return mListData!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = mListData?.get(position)

        /**
         * display name
         */
        holder.itemView.mTextViewName.text = user?.displayName ?: ""

        /**
         * quantity
         */
        holder.itemView.mTextViewQuantity.text = "Quantity : " + user?.quantity.toString()

        holder.itemView.mLinearLayout.setOnClickListener {
            if (mItemInteractionListener != null) {
                mItemInteractionListener?.onInteraction(holder.itemView, user!!, position)
            }
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view)
}