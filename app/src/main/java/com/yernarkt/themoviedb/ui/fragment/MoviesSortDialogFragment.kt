package com.yernarkt.themoviedb.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.yernarkt.themoviedb.R

class MoviesSortDialogFragment : DialogFragment() {
    private var mView: View? = null

    companion object {
        fun newInstance(): MoviesSortDialogFragment {
            return MoviesSortDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(context, R.layout.dialog_fragment_movies_sort, null)
        val alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle(context!!.resources.getString(R.string.s_filter))
        alertDialogBuilder.setView(mView)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        return alertDialogBuilder.create()
    }
}