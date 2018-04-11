package com.zed.example.net.bean

import java.io.Serializable

/**
 * Created by zed on 2018/3/30.
 */


data class HmbbBean(
        val title: Title,
        val addrList: List<Addr>,
        val logoList: List<Logo>,
        val ver: Ver,
        val copyright: Copyright,
        val siteList: List<Site>
)

data class Ver(
        val name: String
)

data class Addr(
        val url: String
) : Serializable

data class Site(
        val message: String,
        val name: String,
        val img: String,
        val url: String
) : BaseBean()

data class Copyright(
        val name: String
)

data class Title(
        val name: String
)

data class Logo(
        val message: String,
        val name: String,
        val img: String,
        val url: String
) : BaseBean()