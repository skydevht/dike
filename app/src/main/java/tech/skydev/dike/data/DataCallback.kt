package tech.skydev.dike.data

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface DataCallback<T> {
    fun onSuccess(result: T)
    fun onError(t: Throwable)
}