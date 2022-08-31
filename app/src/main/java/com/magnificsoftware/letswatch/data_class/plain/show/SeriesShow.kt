package com.magnificsoftware.letswatch.data_class.plain.show

interface SeriesShow {
    val tv_show_id: Int?
    val tv_show_name: String?
    val tv_show_type: Int?
    val content_type: Int?

    fun isSeries(): Boolean {
        return tv_show_id != null
    }
}