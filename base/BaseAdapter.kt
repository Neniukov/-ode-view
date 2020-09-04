package com.allocaterite.allocaterite.core.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<M, VH : BaseViewHolder<M>>(
    val items: MutableList<M> = ArrayList()
) : RecyclerView.Adapter<VH>() {

    private var onClick: ((M?, VH) -> Unit)? = null

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
        onClick?.let { click ->
            holder.itemView.setOnClickListener {
                click(items[position], holder)
            }
        }
    }

    infix fun onClickListener(onClick: (M?, VH) -> Unit) {
        this.onClick = onClick
    }

    fun replace(_items: List<M>) =
        DiffUtil.calculateDiff(BaseDiffCallback(items, _items)).also {
            items.clear()
            items.addAll(_items)
            it.dispatchUpdatesTo(this@BaseAdapter)
        }
}