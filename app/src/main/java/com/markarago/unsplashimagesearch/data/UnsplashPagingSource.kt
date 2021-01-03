package com.markarago.unsplashimagesearch.data

import android.util.Log
import androidx.paging.PagingSource
import com.markarago.unsplashimagesearch.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
): PagingSource<Int, UnsplashPhoto>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        // what page we're currently on (null on first page)
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            // make api call
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            // everything is fine
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1, // no prev key if in first pos
                nextKey = if (photos.isEmpty()) null else position + 1 //if photos are empty = we reach end
            )
        } catch (e: IOException) {
            // no internet exception

            Log.d("UnsplashPagingSource", "load: Internet Error")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // server error
            LoadResult.Error(e)
        }
    }
}