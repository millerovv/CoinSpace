package com.example.alexmelnikov.coinspace.ui.home

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alexmelnikov.coinspace.R
import com.example.alexmelnikov.coinspace.model.entities.Account
import com.example.alexmelnikov.coinspace.model.entities.UserBalance
import com.example.alexmelnikov.coinspace.ui.accounts.AccountsAdapter
import com.example.alexmelnikov.coinspace.util.formatToMoneyString
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.card_account_balance.view.*
import kotlinx.android.synthetic.main.card_current_budget.view.*

/**
 *  Created by Alexander Melnikov on 28.07.18.
 */

class AccountsPagerAdapter(private val mContext: Context,
                           private val mUserBalance: UserBalance,
                           private val mAccounts: ArrayList<Account>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position == 0) {
            val layout = LayoutInflater.from(mContext).inflate(
                    mContext.resources.getLayout(R.layout.card_current_budget), container, false)

            layout.tv_balance_lbl.text = mContext.getText(R.string.tv_balance_lbl)
            layout.tv_main_cur_balance.text = formatToMoneyString(mUserBalance.balance, mUserBalance.currency)
            layout.tv_additional_cur_balance.text = formatToMoneyString(mUserBalance.balanceUsd, "USD")
            layout.tag = BALANCE_VIEW_TAG
            container.addView(layout)
            return layout
        } else {
            val account = mAccounts[position - 1]
            val layout = LayoutInflater.from(mContext).inflate(
                    mContext.resources.getLayout(R.layout.card_account_balance), container, false)
            layout.tv_account_name.text = account.name
            layout.tv_account_balance.text = formatToMoneyString(account.balance, account.currency)

            if (account.name != mContext.resources.getString(R.string.cash_account_name)) {
                layout.iv_account_icon.setImageResource(R.drawable.ic_credit_card_primary_24dp)
                layout.iv_account_icon.setColorFilter(account.color, PorterDuff.Mode.SRC_ATOP)
            } else {
                layout.iv_account_icon.setImageResource(R.drawable.ic_account_balance_wallet_primary_24dp)
            }

            layout.tag = account.id
            container.addView(layout)
            return layout
        }


    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return mAccounts.size + 1
    }

    companion object {
        val BALANCE_VIEW_TAG = "BALANCE_VIEW_TAG"
    }
}