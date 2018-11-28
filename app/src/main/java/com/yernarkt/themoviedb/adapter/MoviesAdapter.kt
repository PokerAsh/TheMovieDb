package com.yernarkt.themoviedb.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.InvocationTargetException
import java.util.*

abstract class MoviesAdapter<T, VH : RecyclerView.ViewHolder>(
    private val mLayout: Int,
    private var mViewHolderClass: Class<VH>,
    private var mModelClass: Class<T>,
    private var mData: ArrayList<T>?
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v1 = LayoutInflater.from(parent.context)
            .inflate(mLayout, parent, false)
        try {
            val constructor = mViewHolderClass.getConstructor(View::class.java)
            return constructor.newInstance(v1)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = getItem(position)
        bindView(holder, model, position)

    }

    protected abstract fun bindView(holder: VH, model: T, position: Int)

    private fun getItem(position: Int): T {
        return mData!![position]
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size
    }

    private fun remove(r: T) {
        val position = mData!!.indexOf(r)
        if (position > -1) {
            mData!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun removeAt(position: Int) {
        mData!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, mData!!.size)
    }
}
