package math.question.task.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DropDownModel (
    var select :Boolean,
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("phone_code")
    @Expose
    var phone_code: String,
    @SerializedName("nationality")
    @Expose
    var nationality: String
)