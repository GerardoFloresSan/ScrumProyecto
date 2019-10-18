package com.example.scrumproyect.data.entity
import java.io.Serializable

data class MasterEntity (
    var forbiddenWords : List<String> = listOf(
        "Xvideos.com",
        "Pornhub.com",
        "Xnxx.com",
        "Serviporno.com",
        "Xhamster.com",
        "Follamigos.com",
        "RedTube.com",
        "Teatroporno.com",
        "Diverporno.com",
        "Soloporno.xxx"
    ),
    var urlGeneral : String = "https://firebasestorage.googleapis.com/v0/b/proyectoscrum-2d2e3.appspot.com/o/background_example.jpg?alt=media",
    var about : String = "",
    var link : String = "https://www.youtube.com"
): Serializable