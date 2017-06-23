package tech.skydev.dike.model

/**
 * Created by hash on 6/18/17.
 */
class Section {
    var name : String? = null
    var type : String? = null
    var order : Int? = 0

    var children : List<Section>? = null
    var contents : List<Content>? = null
}