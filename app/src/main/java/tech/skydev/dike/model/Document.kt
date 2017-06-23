package tech.skydev.dike.model

import org.parceler.Parcel


/**
 * Created by hash on 6/18/17.
 */
@Parcel
class Document{
    var name: String? = null
    var path: String? = null
    var sections: List<Section>? = null
    var contents: List<Content>? = null
}