package com.example.scrumproyect.data.entity

import com.example.scrumproyect.view.ui.utils.linkpewview.MetaDataKotlin
import java.io.Serializable

data class ArticleEntity (
    var idM : String = "",

    var titleM : String = "",
    var descriptionM : String = "",
    var urlImageM : String = "",

    var urlM : String = "",
    var timeCreate : Long = 0,
    var metadata: MetaDataKotlin = MetaDataKotlin(),
    var sad: Long = 0,
    var neutral: Long = 0,
    var happy: Long = 0
) : Serializable