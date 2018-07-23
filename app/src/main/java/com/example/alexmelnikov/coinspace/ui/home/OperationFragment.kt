package com.example.alexmelnikov.coinspace.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.alexmelnikov.coinspace.R
import com.example.alexmelnikov.coinspace.ui.RevealCircleAnimatorHelper
import java.text.DateFormat
import java.util.*
import android.text.Editable
import com.example.alexmelnikov.coinspace.model.Operation


class OperationFragment : Fragment(), HomeContract.OperationView {

    override lateinit var presenter: HomeContract.Presenter

    private lateinit var mExpenseCardLayout: RelativeLayout
    private lateinit var mOperationTypeButtonsLayout: LinearLayout
    private lateinit var mNewOperationLayout: RelativeLayout
    private lateinit var mExpenseButton: Button
    private lateinit var mIncomeButton: Button
    private lateinit var mSumInputLayout: TextInputLayout
    private lateinit var mSumEt: EditText
    private lateinit var mOperationTypeLabel: TextView
    private lateinit var mDateLabel: TextView
    private lateinit var mCurrencySpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_operation, container, false)
        RevealCircleAnimatorHelper
                .create(this, container)
                .start(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mExpenseCardLayout = view.findViewById(R.id.rl_expense_card)
        mOperationTypeButtonsLayout = view.findViewById(R.id.ll_action_type_btns)
        mNewOperationLayout = view.findViewById(R.id.rl_new_action)
        mExpenseButton = view.findViewById(R.id.btn_expense)
        mIncomeButton = view.findViewById(R.id.btn_income)
        mOperationTypeLabel = view.findViewById(R.id.tv_label)
        mCurrencySpinner = view.findViewById(R.id.currency_spinner)
        mDateLabel = view.findViewById(R.id.tv_date)
        mSumInputLayout = view.findViewById(R.id.input_layout_sum)
        mSumEt = view.findViewById(R.id.et_sum)

        setupEventListeners()

        //setupSpinner
        val currencies = resources.getStringArray(R.array.main_currency_values_array)
        val spinnerArrayAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, currencies)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCurrencySpinner.adapter = spinnerArrayAdapter
        mCurrencySpinner.setSelection(currencies.indexOf(presenter.mainCurrency))

        //animate operation card sliding up
        mExpenseCardLayout.postDelayed({
            mExpenseCardLayout.visibility = View.VISIBLE
            YoYo.with(Techniques.SlideInUp)
                    .duration(300)
                    .playOn(mExpenseCardLayout)
        }, 350)

    }

    private fun setupEventListeners() {
        mExpenseButton.setOnClickListener {
            presenter.newExpenseButtonClick()
        }

        mIncomeButton.setOnClickListener {
            presenter.newIncomeButtonClick()
        }

        mSumEt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (mSumInputLayout.isErrorEnabled) mSumInputLayout.error = ""
            }
        })
    }

    override fun setupNewOperationLayout(type: Operation.OperationType) {
        mOperationTypeButtonsLayout.visibility = View.GONE
        mNewOperationLayout.visibility = View.VISIBLE
        mDateLabel.text = DateFormat.getDateTimeInstance().format(Date())
        when (type) {
            Operation.OperationType.Expense -> mOperationTypeLabel.text = getString(R.string.label_new_expense)
            Operation.OperationType.Income ->  mOperationTypeLabel.text = getString(R.string.label_new_income)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachOperationView()
    }

    /**
     * Animate operation card sliding out and call popbackstack when animation ends or canceled
     * @return false if input sum is empty or equals 0
     */
    override fun confirmOperationAndCloseSelf(): Boolean {
        if (mSumEt.text.toString().trim().isEmpty()) {
            mSumInputLayout.error = getString(R.string.empty_sum_error)
            return false
        } else if (mSumEt.text.toString().trim().toFloat() <= 0f) {
            mSumInputLayout.error = getString(R.string.zero_sum_error)
            return false
        } else {
            presenter.newOperationRequest(mSumEt.text.toString().trim().toFloat(),
                    mCurrencySpinner.selectedItem.toString())

            YoYo.with(Techniques.SlideOutUp)
                    .duration(300)
                    .withListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationCancel(animation: Animator) {
                            super.onAnimationCancel(animation)
                            mExpenseCardLayout.postDelayed({ fragmentManager?.popBackStack() }, 50)
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            mExpenseCardLayout.postDelayed({ fragmentManager?.popBackStack() }, 50)
                        }
                    })
                    .playOn(mExpenseCardLayout)

            return true
        }

    }


    companion object {

        fun newInstance(sourceView: View? = null) = OperationFragment().apply {
            arguments = Bundle()
            RevealCircleAnimatorHelper.addBundleValues(arguments!!, sourceView)
        }
    }

}