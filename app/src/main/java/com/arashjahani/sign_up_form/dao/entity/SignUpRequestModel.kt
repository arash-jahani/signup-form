package com.arashjahani.sign_up_form.dao.entity

data class SignUpRequestModel(
	var avatarUri: String?,
	var firstName: String?,
	var email: String,
	var password: String,
	var website: String?,
	)

