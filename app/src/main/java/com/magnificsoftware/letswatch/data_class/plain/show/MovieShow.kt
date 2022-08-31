package com.magnificsoftware.letswatch.data_class.plain.show

interface MovieShow {
    val video_show_id: Int?
    val video_show_title: String?
    val vs_description: String?
    val vs_runtime: String?
    val vs_release_date: String?

    fun isMovie(): Boolean {
        return video_show_id != null
    }
}