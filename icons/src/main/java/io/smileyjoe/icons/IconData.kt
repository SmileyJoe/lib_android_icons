package io.smileyjoe.icons

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class IconData(
        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id: String,

        @ColumnInfo(name = "name")
        @SerializedName("name")
        var name: String? = null,

        @ColumnInfo(name = "path")
        @SerializedName("data")
        var path: String? = null
) {
    /**
     * Check if the icon is valid.
     * <p/>
     * A valid icon has an id and a path.
     *
     * @return whether the icon data is valid or not
     */
    fun isValid(): Boolean {
        return !id.isNullOrEmpty() && !path.isNullOrEmpty()
    }
}
