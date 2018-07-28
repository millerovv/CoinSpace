package com.example.alexmelnikov.coinspace.ui.add_new_account

import com.example.alexmelnikov.coinspace.BaseApp
import com.example.alexmelnikov.coinspace.model.repositories.AccountsRepository
import com.example.alexmelnikov.coinspace.util.PreferencesHelper
import com.example.alexmelnikov.coinspace.util.PreferencesHelper.Companion.MAIN_CURRENCY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddAccountPresenter : AddAccountContract.Presenter {

    lateinit var view: AddAccountContract.View

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var accountsRepository: AccountsRepository

    override var selectedColor: Int = 0

    override fun attach(view: AddAccountContract.View) {
        this.view = view
        BaseApp.instance.component.inject(this)
    }

    override fun mainCurrencyRequest(): String = preferencesHelper.loadString(MAIN_CURRENCY)

    override fun showColorPickerRequest() {
        view.showColorPickerDialog()
    }

    override fun updateSelectedColor(color: Int) {
        selectedColor = color
        view.updateLayoutBgColor(selectedColor)
    }

    override fun addNewAccountButtonClick(name: String, currency: String) {
        //Check if account with similar name is already in the db
        accountsRepository.findAccountByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({account -> view.showEditTextError(true)},
                        {
                            accountsRepository.insertAccountOfflineAsync(name = name, currency = currency,
                                    color = selectedColor)
                            view.closeSelf()
                        })
    }

}