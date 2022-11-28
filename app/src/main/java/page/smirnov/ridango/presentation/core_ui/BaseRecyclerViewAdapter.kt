package page.smirnov.ridango.presentation.core_ui

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import page.smirnov.ridango.util.extension.layoutInflater
import page.smirnov.ridango.util.setOnSingleClickListener

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseRecyclerViewAdapter<
        T,
        VB : ViewBinding,
        VH : BaseRecyclerViewAdapter<T, VB, VH>.ViewHolder,
        >(
    private val inflate: Inflate<VB>,
) : RecyclerView.Adapter<VH>() {

    var items: List<T> = emptyList()
        set(value) {
            if (value != field) {
                updateItems(oldList = field, newList = value)

                field = value

                postUpdateItems()
            }
        }

    var onItemClick: (item: T) -> Unit = {}

    protected open fun updateItems(oldList: List<T>, newList: List<T>) {}

    protected open fun postUpdateItems() {}

    protected open fun createView(parent: ViewGroup, viewType: Int): VB {
        return inflate.invoke(parent.layoutInflater, parent, false)
    }

    protected abstract fun createViewHolder(binding: VB): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createView(parent, viewType)
            .let(::createViewHolder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    /**
     * View holder
     */
    abstract inner class ViewHolder(
        protected val binding: VB,
    ) : RecyclerView.ViewHolder(binding.root) {

        protected var currentItem: T? = null

        protected val context: Context
            get() = binding.root.context

        init {
            binding.root.setOnSingleClickListener {
                currentItem?.let(onItemClick::invoke)
            }
        }

        open fun bind(item: T) {
            currentItem = item
        }
    }
}
