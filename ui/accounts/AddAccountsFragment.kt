package com.allocaterite.allocaterite.feature.main.portfolio.addAccounts

import android.os.Bundle
import android.view.View
import com.allocaterite.allocaterite.R
import com.allocaterite.allocaterite.core.base.BaseActivity
import com.allocaterite.allocaterite.core.base.BaseFragment
import com.allocaterite.allocaterite.feature.main.portfolio.PortfolioViewModel
import com.allocaterite.allocaterite.ui.ItemDivider
import com.allocaterite.allocaterite.utils.extenstions.*
import kotlinx.android.synthetic.main.fragment_add_accounts.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountsFragment : BaseFragment() {

    private val adapter = AddAccountsAdapter()

    private val viewModel: PortfolioViewModel by viewModel()

    override val layoutResId = R.layout.fragment_add_accounts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setColorStatusBar(R.color.bg_container)
        arguments?.let {
            it.getString(BaseActivity.KEY_FRAGMENT_ID)?.apply {
                showProgress()
                viewModel.getAllAccounts(this)
            }
        }
        ivBack.click {
            setColorStatusBar(R.color.light_green_alpha)
            activity?.onBackPressed()
        }
        rvAccounts.adapter = adapter
        rvAccounts.addItemDecoration(ItemDivider(14.dpToPx()))
        observe(viewModel.allAccountsLD) { accounts ->
            hideProgress()
            if (accounts.isEmpty()) clEmptyAcc.show()
            else {
                clEmptyAcc.gone()
                adapter.updateData(accounts)
            }
        }
        adapter.selectAccount = {
            if (viewModel.selectAddAccount(it)) {
                btnAddAccounts.setBackgroundResource(R.drawable.shape_ripple)
                btnAddAccounts.isEnabled = true
            } else {
                btnAddAccounts.isEnabled = false
                btnAddAccounts.setBackgroundResource(R.drawable.shape_bg_light_green)
            }
        }
        btnAddAccounts.click {
            showProgress()
            viewModel.addAccounts()
        }
        observe(viewModel.informationAccLD) {
            hideProgress()
            if (it.newUniqueAccounts > 0) activity?.onBackPressed()
        }
        observe(viewModel.errorLD) { showDialogMessage("Error", it) }
    }

    companion object {
        fun getInstance() = AddAccountsFragment()
    }
}