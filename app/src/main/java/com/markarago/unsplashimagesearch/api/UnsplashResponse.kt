package com.markarago.unsplashimagesearch.api

import com.markarago.unsplashimagesearch.data.UnsplashPhoto

data class UnsplashResponse (
    val results: List<UnsplashPhoto>
)