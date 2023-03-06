package com.forkmang.helper

object Constant {
    var IS_BookTableFragmentLoad = false

    @JvmField
    var SUCCESS_CODE = "200"

    @JvmField
    var SUCCESS_CODE_n = 200

    @JvmField
    var ERROR_CODE = 422

    @JvmField
    var ERROR_CODE_n = 404
    var GUESTUSERlOGIN = 401
    var KEEP_LOGIN = false
    var is_permission_grant = 0

    @JvmField
    var TOKEN_LOGIN = "tokenlogin"

    @JvmField
    var IDENTFIER = "identifier"
    var TOKEN_REG = "tokenreg"
    var TOKEN_FORGOTPASS = "tokenforgotpass"

    @JvmField
    var NAME = "name"

    @JvmField
    var MOBILE = "mobile"

    @JvmField
    var BOOKINGID = "bookingid"
    var CUSTOMERID = "customerid"

    @JvmField
    var CARTID = "cartid"

    @JvmField
    var CART_ITEMID = "cart_itemid"

    @JvmField
    var ENTER_NAME = "Please enter your name"

    @JvmField
    var ENTER_MOBILE = "Please enter mobile no"
    var PASSWORD_MATCH = "password and conform password not same"
    var CNFPASSWORD_MATCH = "Please enter conform password"
    var PASSWORD = "Please enter password and must be grater than 3 char"

    @JvmField
    var VALID_NO = "Please enter valid 10 digit mobile no"
    var MOBILE_PASSWORD = "Please enter mobile no and password"

    @JvmField
    var EmptyEmail = "Please add email address"

    @JvmField
    var VALIDEmail = "Please add valid email address"

    @JvmField
    var ERRORMSG = "Error occur please try again"

    @JvmField
    var NODATA = "No Data Available"
    var NODATA_MATCH = "No match found"

    @JvmField
    var NETWORKEROORMSG = "Check Internet Connection"
    const val KEY_LATITUDE = "latitude"
    const val KEY_LONGITUDE = "longitude"

    const val COMMAND_CART_LIST_VIEW = "COMMAND_CART_LIST_VIEW"
}