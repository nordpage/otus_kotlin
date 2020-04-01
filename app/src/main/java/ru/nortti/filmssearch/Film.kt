package ru.nortti.filmssearch

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Film(
    var name: String = "",
    var image: String = "",
    var desctiption: String = "",
    var isActive: Boolean = false,
    var isFavorites: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        name = parcel.readString()!!,
        image = parcel.readString()!!,
        desctiption = parcel.readString()!!,
        isActive = parcel.readByte() != 0.toByte(),
        isFavorites = parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(desctiption)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isFavorites) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Film

        if (name != other.name) return false

        return true
    }
}