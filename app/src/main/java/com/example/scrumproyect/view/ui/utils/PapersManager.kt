package com.example.scrumproyect.view.ui.utils

import com.example.scrumproyect.BuildConfig
import com.example.scrumproyect.data.entity.MasterEntity
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

    var openLoginWithDetail: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("openLogin", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("openLogin", false)
        }

    var openAddArticle: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("open", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("open", false)
        }

    var masters: MasterEntity
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("masters", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("masters", MasterEntity())
        }
}