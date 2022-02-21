package com.jh.dailypoem.network.bean

import java.io.Serializable

/**
 * Description:BaseBean
 *
 * @author l
 * @date 2020/12/20
 */
open class BaseResult<T>: Serializable {
    var data: T? = null
    var status: String? = null
    var errMessage: String? = null
}