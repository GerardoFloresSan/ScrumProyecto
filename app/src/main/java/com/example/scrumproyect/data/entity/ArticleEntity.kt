package com.example.scrumproyect.data.entity

import com.example.scrumproyect.view.ui.utils.linkpewview.MetaDataKotlin
import java.io.Serializable

data class ArticleEntity (
    var idM : String = "",

    var titleM : String = "",
    var descriptionM : String = "",
    var urlImageM : String = "",

    var urlM : String = "",
    var timeCreate : Int = 0,
    var metadata: MetaDataKotlin = MetaDataKotlin()
) : Serializable