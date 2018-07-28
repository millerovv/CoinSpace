package com.example.alexmelnikov.coinspace.model.repositories

import android.util.Log
import com.example.alexmelnikov.coinspace.model.entities.Account
import com.example.alexmelnikov.coinspace.model.entities.Operation
import com.example.alexmelnikov.coinspace.model.persistance.dao.AccountDao
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultAccountsRepository(private val accountDao: AccountDao) : AccountsRepository {

    /**
     * This method called from BaseApp onCreate
     * It checks if accounts table is empty and add two main accounts (cash and card)
     */
    override fun initAddTwoMainAccountsIfTableEmptyAsync(cashName: String, mainCurrency: String, color: Int,
                                                         cardName: String) {
        Completable.fromAction {
            if (accountDao.getAll().isEmpty()) {
                val cashAccount = Account(null, cashName, mainCurrency, 0f, color, ArrayList())
                val cardAccount = Account(null, cardName, mainCurrency, 0f, color, ArrayList())
                accountDao.insert(cashAccount)
                accountDao.insert(cardAccount)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("mytag", "success") },
                        {Log.d("mytag", "error")})
    }

    override fun getAccountsOffline(): Single<List<Account>> {
        return Single.create<List<Account>> {
            it.onSuccess(accountDao.getAll())
        }
    }

    override fun findAccountByName(name: String): Single<Account> {
        return Single.create<Account> {
            it.onSuccess(accountDao.findByName(name))
        }
    }

    override fun insertAccountOfflineAsync(name: String, currency: String, balance: Float, color: Int, operations: List<Operation>) {
        Completable.fromAction {
            val newAccount = Account(null, name, currency, balance, color, operations)
            accountDao.insert(newAccount)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("mytag", "success") },
                        {Log.d("mytag", "error")})
    }

    override fun updateAccountOfflineAsync() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}