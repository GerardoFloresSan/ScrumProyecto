package com.example.scrumproyect.data.entity

import java.io.Serializable

data class UserEntity (
    var uidUser : String = "",
    var email : String = "",
    var type : Int = 0,
    var name : String  = "",
    var urlUser : String  = ""
): Serializable