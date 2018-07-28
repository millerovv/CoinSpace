package com.example.alexmelnikov.coinspace.ui.accounts

import com.example.alexmelnikov.coinspace.model.entities.Account
import com.example.alexmelnikov.coinspace.ui.BaseContract

class AccountsContract {

    interface AccountsView : BaseContract.View {

        var presenter: AccountsContract.Presenter

        fun openAddAccountFragment()

        fun replaceAccountsRecyclerData(accounts: List<Account>)

    }

    //interface View
    interface Presenter : BaseContract.Presenter<AccountsView> {

        fun addNewAccountButtonClick()

        fun accountsDataRequest(updateLayout: Boolean)

    }
    interface View
}