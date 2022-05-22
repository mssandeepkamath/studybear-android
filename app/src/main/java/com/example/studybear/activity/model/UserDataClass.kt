package com.example.studybear.activity.model

data class UserDataClass(
    var name: String?,
    var email: String?,
    var phonenumber: String?,
    var photourl: String?,
    var paidbit: Boolean?,
    var semester:Int,
    var totaluploads:Int?,
    var totalviews:Int?,
    var extrapoints:Int?,
    var razorpayorderid:String?,
    var razorpaysignature:String?,
    var razorpaypaymentid:String?
)
