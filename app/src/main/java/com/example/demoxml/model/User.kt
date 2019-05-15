package com.example.demoxml.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class User : Serializable {
    @SerializedName("write_date")
    @Expose
    var writeDate: String? = null
    @SerializedName("date_submitted")
    @Expose
    var dateSubmitted: Boolean? = null
    @SerializedName("write_uid")
    @Expose
    var writeUid: List<Any>? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("__last_update")
    @Expose
    var lastUpdate: String? = null
    @SerializedName("user_name")
    @Expose
    var userName: Boolean? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("is_checked")
    @Expose
    var isChecked: Boolean? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("display_name")
    @Expose
    var displayName: String? = null
    @SerializedName("create_date")
    @Expose
    var createDate: String? = null
    @SerializedName("create_uid")
    @Expose
    var createUid: List<Any>? = null
}
