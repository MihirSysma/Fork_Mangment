package com.forkmang.data

class CartBooking {
    //String str_productname,str_qty,str_type;
    var data_userid: String? = null
    var data_booking_table_id: String? = null
    var data_total: String? = null
    var cart_item_cartid: String? = null
    var cart_item_id: String? = null
    var cart_item_extra_id: String? = null
    var cart_item_qty: String? = null
    var cart_item_details_category_id: String? = null
    var cart_item_details_name: String? = null
    var cart_item_details_price: String? = null
    var cart_item_details_image: String? = null
    var extra_item_details_name: String? = null
    var extra_item_details_price: String? = null

    /*public CartBooking() {
     }
 
     public CartBooking(Parcel in) {
         super();
         readFromParcel(in);
     }
 
     public static final Parcelable.Creator<CartBooking> CREATOR = new Parcelable.Creator<CartBooking>()
     {
         public CartBooking createFromParcel(Parcel in) {
             return new CartBooking(in);
         }
 
         public CartBooking[] newArray(int size) {
 
             return new CartBooking[size];
         }
 
 
 
     };
 
     public void readFromParcel(Parcel in) {
         data_userid = in.readString();
         data_booking_table_id = in.readString();
         data_total = in.readString();
         cart_item_cartid = in.readString();
         cart_item_id = in.readString();
         cart_item_extra_id = in.readString();
         cart_item_qty = in.readString();
         cart_item_details_category_id = in.readString();
         cart_item_details_name = in.readString();
         cart_item_details_price = in.readString();
         cart_item_details_image = in.readString();
         extra_item_details_name = in.readString();
         extra_item_details_price = in.readString();
         extra_item_details_item_id = in.readString();
 
 
     }
     public int describeContents() {
         return 0;
     }
 
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(data_userid);
         dest.writeString(data_booking_table_id);
         dest.writeString(data_total);
         dest.writeString(cart_item_cartid);
         dest.writeString(cart_item_id);
         dest.writeString(cart_item_extra_id);
         dest.writeString(cart_item_qty);
         dest.writeString(cart_item_details_category_id);
         dest.writeString(cart_item_details_name);
         dest.writeString(cart_item_details_price);
         dest.writeString(cart_item_details_image);
         dest.writeString(extra_item_details_name);
         dest.writeString(extra_item_details_price);
         dest.writeString(extra_item_details_item_id);
 
     }*/
    var extra_item_details_item_id: String? = null
}