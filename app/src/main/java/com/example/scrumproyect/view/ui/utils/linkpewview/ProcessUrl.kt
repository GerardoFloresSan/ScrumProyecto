package com.example.scrumproyect.view.ui.utils.linkpewview

import android.content.Context
import android.os.AsyncTask
import android.webkit.URLUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException
import java.net.URI
import java.net.URISyntaxException

class ProcessUrl(private val doStuff: DoStuff) : AsyncTask<Any, Void, MetaDataKotlin>() {
    private lateinit var metadata: MetaDataKotlin

    @Suppress("SENSELESS_COMPARISON")
    override fun doInBackground(vararg objects: Any): MetaDataKotlin {
        try {
            @Suppress("CanBeVal")
            var doc: Document?
            val url = objects[0] as String
            metadata = MetaDataKotlin()

            doc = Jsoup.connect(url).timeout(60 * 1000
            ).get()

            val elements = doc!!.getElementsByTag("meta")

            // getTitle doc.select("meta[property=og:title]")
            var title: String? = doc.select("meta[property=og:title]").attr("content")

            if (title == null || title.isEmpty()) {
                title = doc.title()
            }
            metadata.title = title!!

            //getDescription
            var description: String? = doc.select("meta[name=description]").attr("content")
            if (description!!.isEmpty() || description == null) {
                description = doc.select("meta[name=Description]").attr("content")
            }
            if (description!!.isEmpty() || description == null) {
                description = doc.select("meta[property=og:description]").attr("content")
            }
            if (description!!.isEmpty() || description == null) {
                description = ""
            }
            metadata.description = description


            // getMediaType
            val mediaTypes = doc.select("meta[name=medium]")
            val type: String
            type = if (mediaTypes.size > 0) {
                val media = mediaTypes.attr("content")
                if (media == "image") "photo" else media
            } else {
                doc.select("meta[property=og:type]").attr("content")
            }
            metadata.mediaType = type


            //getImages
            val imageElements = doc.select("meta[property=og:image]")
            if (imageElements.size > 0) {
                val image = imageElements.attr("content")
                if (image.isNotEmpty()) {
                    metadata.imageUrl = (resolveURL(url, image))
                }
            }
            if (metadata.imageUrl.isEmpty()) {
                var src = doc.select("link[rel=image_src]").attr("href")
                if (src.isNotEmpty()) {
                    metadata.imageUrl = (resolveURL(url, src))
                } else {
                    src = doc.select("link[rel=apple-touch-icon]").attr("href")
                    if (src.isNotEmpty()) {
                        metadata.imageUrl = (resolveURL(url, src))
                        metadata.favicon = (resolveURL(url, src))
                    } else {
                        src = doc.select("link[rel=icon]").attr("href")
                        if (src.isNotEmpty()) {
                            metadata.imageUrl = (resolveURL(url, src))
                            metadata.favicon =  (resolveURL(url, src))
                        }
                    }
                }
            }

            //Favicon
            var src = doc.select("link[rel=apple-touch-icon]").attr("href")
            if (src.isNotEmpty()) {
                metadata.favicon = resolveURL(url, src)
            } else {
                src = doc.select("link[rel=icon]").attr("href")
                if (src.isNotEmpty()) {
                    metadata.favicon = resolveURL(url, src)
                }
            }

            for (element in elements) {
                if (element.hasAttr("property")) {
                    @Suppress("LocalVariableName")
                    val str_property = element.attr("property").toString().trim { it <= ' ' }
                    if (str_property == "og:url") {
                        metadata.url = element.attr("content").toString()
                    }
                    if (str_property == "og:site_name") {
                        metadata.siteName = element.attr("content").toString()
                    }
                }
            }

            if (metadata.url == "" || metadata.url.isEmpty()) {
                var uri: URI? = null
                try {
                    uri = URI(url)
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

                @Suppress("SENSELESS_COMPARISON")
                if (url == null) {
                    metadata.url = url
                } else {
                    metadata.url = uri!!.host
                }
            }
            return metadata
        } catch (e : SocketTimeoutException) {
            // la url no responde
            return MetaDataKotlin().apply {
                typeError = 1
            }
        } catch (e : IllegalArgumentException) {
            // url podria no ser valida
            return MetaDataKotlin().apply {
                typeError = 2
            }
        } catch (e : Exception) {
            return MetaDataKotlin().apply {
                typeError = 3
            }
        }
    }

    private fun resolveURL(url: String, part: String): String {
        return if (URLUtil.isValidUrl(part)) {
            part
        } else {
            @Suppress("LocalVariableName")
            var base_uri: URI? = null
            try {
                base_uri = URI(url)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            base_uri = base_uri!!.resolve(part)
            base_uri!!.toString()
        }
    }

    override fun onPostExecute(result: MetaDataKotlin?) {
        super.onPostExecute(result)
        doStuff.done(result ?: MetaDataKotlin())
    }


    interface DoStuff {
        fun getContext(): Context
        fun done(result: MetaDataKotlin)
    }
}