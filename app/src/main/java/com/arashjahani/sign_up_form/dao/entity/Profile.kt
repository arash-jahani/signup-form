package com.arashjahani.sign_up_form.dao.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
	var avatarUri: String?,
	var firstName: String?,
	var email: String,
	var website: String?,
	):Parcelable

