package com.yihs.dailycashflow.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.model.Transaction
import com.yihs.dailycashflow.databinding.ItemEmptyTransactionHistoryBinding
import com.yihs.dailycashflow.databinding.ItemTransactionHistoryBinding
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper


class HomeAdapter: ListAdapter<Transaction, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onClickItem: ((Transaction) -> Unit)? = null

    override fun getItemCount(): Int {
        return if(super.getItemCount() == 0) 1 else super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {

        return if (super.getItemCount() == 0) {
            VIEW_TYPE_EMPTY
        } else {
            VIEW_TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemTransactionHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HomeViewHolder(binding)
        } else {
            val binding = ItemEmptyTransactionHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            EmptyViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HomeViewHolder && super.getItemCount() > 0){
            val item = getItem(position)
            //bind item to view
            holder.bind(item)
            //set On Click Item
            holder.itemView.setOnClickListener { onClickItem?.invoke(item) }
        }

    }




    class HomeViewHolder(private val binding: ItemTransactionHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        //default color value
        private val colorValueDefault: Int by lazy {
            Helper.getColorFromAttr(
                binding.root.context,
                R.attr.colorValueDefaultItemTransactionHistory,
                Color.BLUE
            )
        }

        private val iconValueDefault: Drawable? by lazy {
            Helper.getDrawable(
                binding.root.context,
                R.drawable.ic_money
            )
        }


        //get color value income
        private val colorValueIncome: Int by lazy {
            Helper.getColorFromAttr(
                binding.root.context,
                R.attr.colorValueIncomeItemTransactionHistory,
                Color.GREEN
            )
        }

        //get icon value income
        private val iconValueIncome: Drawable? by lazy {
            Helper.getDrawable(binding.root.context, R.drawable.ic_money_plus_line)
        }

        //get color value expense
        private val colorValueExpense: Int by lazy {
            Helper.getColorFromAttr(
                binding.root.context,
                R.attr.colorValueExpenseItemTransactionHistory,
                Color.RED
            )
        }

        //get icon value expense
        private val iconValueExpense: Drawable? by lazy {
            Helper.getDrawable(binding.root.context, R.drawable.ic_money_minus_line)
        }

        fun bind(data : Transaction){
            val context = binding.root.context

            binding.apply {
                tvTitle.text = data.category.name
                tvDesc.text = data.description
                tvDate.text = Helper.convertTimeStampToStringDate(data.date.toLong(), true)

                when(data.category.type){
                    Constant.CATEGORY_TYPE_INCOME -> {
                        ivIcon.setImageDrawable(iconValueIncome)
                        ivIcon.imageTintList = ColorStateList.valueOf(colorValueIncome)
                        tvAmount.text = String.format(context.getString(R.string.amount_item_income_transaction), Helper.toRupiah(data.amount))
                        tvAmount.setTextColor(colorValueIncome)
                    }
                    Constant.CATEGORY_TYPE_EXPENSE -> {
                        ivIcon.setImageDrawable(iconValueExpense)
                        ivIcon.imageTintList = ColorStateList.valueOf(colorValueExpense)
                        tvAmount.text = String.format(context.getString(R.string.amount_item_expense_transaction), Helper.toRupiah(data.amount))
                        tvAmount.setTextColor(colorValueExpense)
                    }
                    else -> {
                        ivIcon.setImageDrawable(iconValueDefault)
                        ivIcon.imageTintList = ColorStateList.valueOf(colorValueDefault)
                        tvAmount.text = Helper.toRupiah(data.amount)
                        tvAmount.setTextColor(colorValueDefault)
                    }
                }
            }
        }
    }

    //view holder for empty transaction
    class EmptyViewHolder(binding: ItemEmptyTransactionHistoryBinding): RecyclerView.ViewHolder(binding.root)

    companion object{

        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_ITEM = 1
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>(){
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }

        }
    }

}