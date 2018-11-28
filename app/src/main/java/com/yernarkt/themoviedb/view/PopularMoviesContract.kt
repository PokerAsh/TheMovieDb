package com.yernarkt.themoviedb.view

import com.yernarkt.themoviedb.model.PopularMoviesResponse

class PopularMoviesContract {
    interface View : IBaseView{
        fun loadDiscoverMovie(movieList: PopularMoviesResponse)

        fun loadMoreDiscoverMovie(movieList: PopularMoviesResponse)
    }

    interface Presenter{
        fun onViewReady()

        fun decideLoadMore(totalCount: Int)
    }
}