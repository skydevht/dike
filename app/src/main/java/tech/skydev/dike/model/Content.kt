package tech.skydev.dike.model

import org.parceler.Parcel

/**
 * Created by hash on 6/18/17.
 */
@Parcel(Parcel.Serialization.BEAN)
class Content {
    var name: String? = null
    var path: String? = null
    var order: Int = 0
}
