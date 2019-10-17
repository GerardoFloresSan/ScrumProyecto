package com.example.scrumproyect.view.ui.utils

import com.example.scrumproyect.BuildConfig
import com.example.scrumproyect.data.entity.UserEntity
import io.paperdb.Paper

object PapersManager {
    var userEntity: UserEntity
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("user", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("user", UserEntity())
        }
    var session: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("session", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("session", false)
        }
}