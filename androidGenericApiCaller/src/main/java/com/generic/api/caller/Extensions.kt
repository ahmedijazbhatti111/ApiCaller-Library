package com.generic.api.caller

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.coroutineScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.StateFlow

inline fun <reified T>StateFlow<ModelState<String>>.collectValue(
    activity: FragmentActivity,
    isDefaultLoader:Boolean = false,
    noinline onLoading: (() -> Unit)? = null,
    noinline onError: ((String) -> Unit)? = null,
    noinline onSuccess: ((T) -> Unit)
) {
    activity.lifecycle.coroutineScope.launchWhenCreated {
        var progressDialog : ProgressDialog? = null
        if(isDefaultLoader) {
            progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("Loading...")
        }
        collect {
            if (it.isLoading) {
                onLoading?.invoke()
                progressDialog?.show()
            }
            if (it.error.isNotBlank()) {
                progressDialog?.dismiss()
                Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                Log.d("api error", it.error)
                onError?.invoke(it.error)
            }
            it.data?.let { data ->
                progressDialog?.dismiss()
             //   Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                onSuccess(data.toPojo())
            }
        }
    }
}

inline fun <reified T,reified R> StateFlow<ModelState<T>>.collectResponse(
    activity: FragmentActivity,
    isDefaultLoader:Boolean = false,
    noinline onLoading: (() -> Unit)? = null,
    noinline onError: ((String) -> Unit)? = null,
    noinline onSuccess: ((T) -> Unit)
) {
    activity.lifecycle.coroutineScope.launchWhenCreated {
        var progressDialog : ProgressDialog? = null
        if(isDefaultLoader) {
            progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("Loading...")
        }
        collect {
            if (it.isLoading) {
                onLoading?.invoke()
                progressDialog?.show()
            }
            if (it.error.isNotBlank()) {
                progressDialog?.dismiss()
                Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                Log.d("api error", it.error)
                onError?.invoke(it.error)
            }
            it.data?.let { data ->
                progressDialog?.dismiss()
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                onSuccess(data.toJson().toPojo())
            }
        }
    }
}


val gson = Gson()

//fun String.toPojo(cls: Class<*>): Class<*> {
//    return gson.fromJson(this, cls.javaClass)
//}

inline fun <reified T> String.toPojo(): T {
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T> T.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> T.arrayToJson(): String {
    return gson.toJson(this).removeSurrounding("[", "]")
}

fun String.toHashMap(): HashMap<String, String> {
    val jsonStr = this/*.removeSurrounding("[","]")*/
    val list = jsonStr.toPojo<Body>()
    return if (list[0].first.isNullOrEmpty())
        jsonStr.removeSurrounding("[","]").toPojo()
    else {
        HashMap<String, String>().also {hashMap->
            list.forEach {
                hashMap[it.first!!] = it.second!!
            }
        }
    }
}

class Body : ArrayList<BodyItem>()
data class BodyItem(
    val first: String?,
    val second: String?
)

fun Any.paramsConcatenate(): String {
    val sb = StringBuilder().also { it.append("/?") }
    this.arrayToJson().toHashMap().forEach {
        sb.append("${it.key}=${it.value}&")
    }
    return sb.toString().removeSuffix("&")
}

fun Any.hasData(): Boolean {
    return this.toJson() != "[[[]]]"
}

@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    var isConnected = false // Initial Value
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}