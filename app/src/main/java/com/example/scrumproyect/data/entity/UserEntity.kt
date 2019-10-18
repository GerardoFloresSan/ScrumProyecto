package com.example.scrumproyect.data.entity

import java.io.Serializable

data class UserEntity (
    var id : String = "",
    var email : String = "",
    var type : Int = 0
): Serializable