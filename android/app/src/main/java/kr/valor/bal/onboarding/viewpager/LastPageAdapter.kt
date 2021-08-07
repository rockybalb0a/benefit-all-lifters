package kr.valor.bal.onboarding.viewpager

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.data.local.user.UserRecordData

class LastPageAdapter: ListAdapter<UserRecordData, LastPageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastPageViewHolder {
        return when(viewType) {
            CONTENT -> ContentViewHolder.create(parent)
            FOOTER -> FooterViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: LastPageViewHolder, position: Int) {
        when(holder) {
            is ContentViewHolder -> {
                val item = getItem(position) as UserRecordData.Content
                holder.bind(item.userPrRecord)
            }
            is FooterViewHolder -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is UserRecordData.Content -> CONTENT
            is UserRecordData.Footer -> FOOTER
        }
    }

    companion object {

        private const val CONTENT = 0
        private const val FOOTER = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserRecordData>() {
            override fun areItemsTheSame(
                oldItem: UserRecordData,
                newItem: UserRecordData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UserRecordData,
                newItem: UserRecordData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}