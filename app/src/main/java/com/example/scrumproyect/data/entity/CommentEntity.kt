package com.example.scrumproyect.data.entity

import java.io.Serializable

data class CommentEntity (
    var id : String = "",
    var comment : String = "",
    var nameUser : String = "",
    var urlUser : String = "",
    var time : Long = 0
): Serializable