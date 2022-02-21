package com.jh.dailypoem.network

import java.io.Serializable

class PoemBean: Serializable {
    var id: String? = null
    var content: String? = null
    var popularity = 0
    var origin: OriginBean? = null
    var matchTags: List<String>? = null
    var recommendedReason: String? = null
    var cacheAt: String? = null

    class OriginBean: Serializable {
        var title: String? = null
        var dynasty: String? = null
        var author: String? = null
        var content: List<String>? = null
        var translate: List<String>? = null
    }
}