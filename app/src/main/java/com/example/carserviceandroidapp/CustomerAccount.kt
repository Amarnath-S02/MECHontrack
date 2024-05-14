package com.example.carserviceandroidapp

class CustomerAccount {
    var cName: String? = null
    var cPassword: String? = null
    var cEmail: String? = null
    var cMobile: String? = null
    var cAddress: String? = null

    constructor()
    constructor(
        cName: String?,
        cPassword: String?,
        cEmail: String?,
        cMobile: String?,
        cAddress: String?
    ) {
        this.cName = cName
        this.cPassword = cPassword
        this.cEmail = cEmail
        this.cMobile = cMobile
        this.cAddress = cAddress
    }

    fun getcName(): String? {
        return cName
    }

    fun setcName(cName: String?) {
        this.cName = cName
    }

    fun getcPassword(): String? {
        return cPassword
    }

    fun setcPassword(cPassword: String?) {
        this.cPassword = cPassword
    }

    fun getcEmail(): String? {
        return cEmail
    }

    fun setcEmail(cEmail: String?) {
        this.cEmail = cEmail
    }

    fun getcMobile(): String? {
        return cMobile
    }

    fun setcMobile(cMobile: String?) {
        this.cMobile = cMobile
    }

    fun getcAddress(): String? {
        return cAddress
    }

    fun setcAddress(cAddress: String?) {
        this.cAddress = cAddress
    }
}
