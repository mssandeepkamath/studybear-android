package com.example.studybear.activity.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DatabaseReference
import java.io.Serializable

data class DatabaseReferenceClass(val reference: DatabaseReference?):Serializable