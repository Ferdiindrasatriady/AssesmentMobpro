package com.ferdi0054.galeriseni.model

data class ImageResponse(val image: Data)
data class Data(val thumb: Thumbnail, val url: String)
data class Thumbnail(val url: String)