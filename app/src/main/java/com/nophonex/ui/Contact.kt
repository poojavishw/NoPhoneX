package com.nophonex.ui

class Contact{


    private var ContactName: String? = null
    private var ContactNumber: String? = null

    fun getContactName(): String? {
        return ContactName
    }

    fun setContactName(contactName: String) {
        ContactName = contactName
    }

    fun getContactNumber(): String? {
        return ContactNumber
    }

    fun setContactNumber(contactNumber: String) {
        ContactNumber = contactNumber
    }
}