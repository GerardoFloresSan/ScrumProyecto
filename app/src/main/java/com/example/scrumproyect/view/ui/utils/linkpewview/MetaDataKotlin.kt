package com.example.scrumproyect.view.ui.utils.linkpewview
import java.io.Serializable

data class MetaDataKotlin (
    var url : String  = "",
    var imageUrl : String  = "",
    var title : String  = "",
    var description : String  = "",
    var siteName : String  = "",
    var mediaType : String  = "",
    var favicon : String  = "",
    var typeError: Int = 0
) : Serializable