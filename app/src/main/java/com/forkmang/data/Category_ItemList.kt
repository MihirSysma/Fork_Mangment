package com.forkmang.data

import java.io.Serializable

class Category_ItemList : Serializable {
    var id: String? = null
    var category_id: String? = null
    var name: String? = null
    var price: String? = null
    var image: String? = null
    var extra_toppingArrayList: ArrayList<Extra_Topping>? = null
}