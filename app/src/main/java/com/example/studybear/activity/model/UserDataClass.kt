package com.example.studybear.activity.model

data class UserDataClass(
    var name: String?,
    var email: String?,
    var phonenumber: String?,
    var photourl: String?,
    var paidbit: Boolean?,
    var semester:Int?,
    var totaluploads:Long?,
    var totalviews:Long?,
    var extrapoints:Long?,
    var razorpayorderid:String?,
    var razorpaysignature:String?,
    var razorpaypaymentid:String?
)
