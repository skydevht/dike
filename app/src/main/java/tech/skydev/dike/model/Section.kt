package tech.skydev.dike.model

import org.parceler.Parcel

/**
 * Created by hash on 6/18/17.
 */
@Parcel(Parcel.Serialization.BEAN)
class Section {
    var name : String? = null
    var type : String? = null
    var order : Int = 0

    var children : List<Section>? = null
    var contents : List<Content>? = null
}