package com.example.scrumproyect.data.entity

import java.io.Serializable

data class ProductEntity (
    var idM : String = "",
    var titleM : String = "",
    var descriptionM : String = "",
    var urlImageM : String = ""
) : Serializable