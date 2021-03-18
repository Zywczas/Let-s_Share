package com.zywczas.letsshare.model

data class User(
    val auth_id: String = "",
    val name: String = "",
    val email: String = "",
    val date_created: String = "",
    val groups: Array<String>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (auth_id != other.auth_id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = auth_id.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}