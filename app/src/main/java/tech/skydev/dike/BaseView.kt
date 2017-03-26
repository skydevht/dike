package tech.skydev.dike

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface BaseView<T> {
    fun setPresenter(presenter: T)
}