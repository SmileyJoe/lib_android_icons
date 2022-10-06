package io.smileyjoe.icons;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class IconData {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    private String mId;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String mName;
    @ColumnInfo(name = "path")
    @SerializedName("data")
    private String mPath;

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    /**
     * Check if the icon is valid.
     * <p/>
     * A valid icon has an id and a path.
     *
     * @return whether the icon data is valid or not
     */
    public boolean isValid(){
        return !TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mPath);
    }

    @Override
    public String toString() {
        return "IconData{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mPath='" + mPath + '\'' +
                '}';
    }
}
