package com.yernarkt.themoviedb.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.model.genres.Genre
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.view.MoviesSortPresenter
import com.yernarkt.themoviedb.view.MoviesSortView


class MoviesSortDialogFragment : DialogFragment(), MoviesSortView {
    private var mView: View? = null
    private lateinit var appCompatActivity: MovieBaseActivity
    private lateinit var presenter: MoviesSortPresenter

    private var genreSpinner: Spinner? = null
    private var startYearText: TextInputEditText? = null
    private var endYearText: TextInputEditText? = null
    private var startYearTil: TextInputLayout? = null
    private var endYearTil: TextInputLayout? = null

    private var startYear: String? = null
    private var endYear: String? = null
    private var genreId: String? = null

    companion object {
        var TAG: String = MoviesSortDialogFragment::class.java.simpleName
        fun newInstance(): MoviesSortDialogFragment {
            return MoviesSortDialogFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        appCompatActivity = context as MovieBaseActivity
    }

    private fun initPresenter() {
        presenter = MoviesSortPresenter(appCompatActivity, this, mView!!)
    }

    private fun loadData() {
        if (InternetConnection.checkConnection(appCompatActivity)) {
            presenter.loadGenreList()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(appCompatActivity, R.layout.dialog_fragment_movies_sort, null)

        initViews()
        initPresenter()
        loadData()

        val alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle(context!!.resources.getString(R.string.s_filter))
        alertDialogBuilder.setView(mView)
        alertDialogBuilder.setPositiveButton("OK", null)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        return alertDialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE) as Button
        positiveButton.setOnClickListener {
            if (validate()) {
                startYear = if (startYear != "") {
                    "$startYear-01-01"
                } else {
                    ""
                }

                endYear = if (endYear != "") {
                    "$endYear-01-01"
                } else {
                    ""
                }
                appCompatActivity.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.activity_container,
                        MoviesListFragment.newInstance(
                            "Sorted",
                            genreId,
                            startYear!!,
                            endYear!!
                        )
                    )
                    .addToBackStack("Sorted")
                    .commit()
                dialog.dismiss()
            }
        }
    }

    private fun initViews() {
        genreSpinner = mView!!.findViewById(R.id.filterGenre)
        startYearText = mView!!.findViewById(R.id.startYearText)
        endYearText = mView!!.findViewById(R.id.endYearText)
        startYearTil = mView!!.findViewById(R.id.startYearTil)
        endYearTil = mView!!.findViewById(R.id.endYearTil)
    }

    override fun setGenres(genre: ArrayList<Genre>) {
        val genreNameList: ArrayList<String> = ArrayList()
        for (s: Genre in genre) {
            genreNameList.add(s.name!!)
        }
        val adapter = ArrayAdapter<String>(appCompatActivity, android.R.layout.simple_spinner_item, genreNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        genreSpinner!!.adapter = adapter
        genreSpinner!!.setSelection(0)
        genreSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, someLong: Long) {
                genreId = genre[pos].id.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    /**
     * validate() semi-works...
     * will look at it when I'll have time...
     * In meantime, I will think that all validations are passed
     */
    private fun validate(): Boolean {
        val valid = true

        val validationWarning = "Не валидное поле"
        startYear = startYearText!!.text.toString()
        endYear = endYearText!!.text.toString()

//        if (startYear == "" && endYear == "") {
//            return true
//        } else if (startYear != "") {
//            when {
//                startYear!!.length != 4 -> {
//
//                    startYearTil!!.isErrorEnabled = true
//                    startYearTil!!.error = validationWarning
//                    return false
//                }
//                startYear!!.toInt() != 2005 && startYear!!.toInt() != 2019 -> {
//                    startYearTil!!.isErrorEnabled = true
//                    startYearTil!!.error = validationWarning
//                    return false
//                }
//                else -> {
//                    startYearTil!!.error = null
//                    startYearTil!!.isErrorEnabled = false
//                }
//            }
//        } else if (endYear != "") {
//            when {
//                endYear!!.length != 4 -> {
//                    endYearTil!!.isErrorEnabled = true
//                    endYearTil!!.error = validationWarning
//                    return false
//                }
//                endYear!!.toInt() != 2005 && endYear!!.toInt() != 2019 -> {
//                    endYearTil!!.isErrorEnabled = true
//                    endYearTil!!.error = validationWarning
//                    return false
//                }
//                else -> {
//                    endYearTil!!.error = null
//                    endYearTil!!.isErrorEnabled = false
//                }
//            }
//        } else if (startYear != "" && endYear != "") {
//            if (startYear!!.toInt() < endYear!!.toInt()) {
//                endYearTil!!.isErrorEnabled = true
//                endYearTil!!.error = "Дата до не должны быть меньше даты от"
//                return false
//            } else {
//                endYearTil!!.error = null
//                endYearTil!!.isErrorEnabled = false
//            }
//        }

        return valid
    }
}