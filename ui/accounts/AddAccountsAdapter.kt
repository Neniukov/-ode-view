package com.allocaterite.allocaterite.feature.main.portfolio.addAccounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allocaterite.allocaterite.R
import com.allocaterite.allocaterite.core.entity.AccountForConnect
import com.allocaterite.allocaterite.utils.extenstions.click
import com.allocaterite.allocaterite.utils.extenstions.toRound
import kotlinx.android.synthetic.main.item_edit_account.view.*

class AddAccountsAdapter : RecyclerView.Adapter<AddAccountsAdapter.AddAccountsViewHolder>() {

    var accounts = arrayListOf<AccountForConnect>()
    var selectAccount: ((item: AccountForConnect) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAccountsViewHolder {
        return AddAccountsViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.item_edit_account, parent, false)
        )
    }

    override fun getItemCount(): Int = accounts.size

    override fun onBindViewHolder(holder: AddAccountsViewHolder, position: Int) {
        val currentAccount = accounts[position]
        holder.bind(currentAccount, position)
    }

    fun updateData(accounts: List<AccountForConnect>) {
        this.accounts.clear()
        this.accounts.addAll(accounts)
        notifyDataSetChanged()
    }

    inner class AddAccountsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(currentAccount: AccountForConnect, position: Int) {
            itemView.apply {
                click {
                    selectAccount?.invoke(currentAccount)
                    checkbox.isChecked = !checkbox.isChecked
                }
                tvAccountName.text = currentAccount.name
                tvSum.text =
                    "${itemView.context.getString(
                        R.string.price_value,
                        currentAccount.balance.toRound()
                    )}"
            }
        }
    }
}