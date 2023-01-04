package com.forkmang.data;

public class CartBooking  {

    //String str_productname,str_qty,str_type;
    String data_userid, data_booking_table_id, data_total;

    String cart_item_cartid, cart_item_id, cart_item_extra_id, cart_item_qty;

    String cart_item_details_category_id, cart_item_details_name, cart_item_details_price, cart_item_details_image;

    String extra_item_details_name,extra_item_details_price,extra_item_details_item_id;

    public String getCart_item_qty() {
        return cart_item_qty;
    }

    public void setCart_item_qty(String cart_item_qty) {
        this.cart_item_qty = cart_item_qty;
    }

    public String getData_userid() {
        return data_userid;
    }

    public void setData_userid(String data_userid) {
        this.data_userid = data_userid;
    }

    public String getData_booking_table_id() {
        return data_booking_table_id;
    }

    public void setData_booking_table_id(String data_booking_table_id) {
        this.data_booking_table_id = data_booking_table_id;
    }

    public String getData_total() {
        return data_total;
    }

    public void setData_total(String data_total) {
        this.data_total = data_total;
    }

    public String getCart_item_cartid() {
        return cart_item_cartid;
    }

    public void setCart_item_cartid(String cart_item_cartid) {
        this.cart_item_cartid = cart_item_cartid;
    }

    public String getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(String cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public String getCart_item_extra_id() {
        return cart_item_extra_id;
    }

    public void setCart_item_extra_id(String cart_item_extra_id) {
        this.cart_item_extra_id = cart_item_extra_id;
    }



    public String getCart_item_details_category_id() {
        return cart_item_details_category_id;
    }

    public void setCart_item_details_category_id(String cart_item_details_category_id) {
        this.cart_item_details_category_id = cart_item_details_category_id;
    }

    public String getCart_item_details_name() {
        return cart_item_details_name;
    }

    public void setCart_item_details_name(String cart_item_details_name) {
        this.cart_item_details_name = cart_item_details_name;
    }

    public String getCart_item_details_price() {
        return cart_item_details_price;
    }

    public void setCart_item_details_price(String cart_item_details_price) {
        this.cart_item_details_price = cart_item_details_price;
    }

    public String getCart_item_details_image() {
        return cart_item_details_image;
    }

    public void setCart_item_details_image(String cart_item_details_image) {
        this.cart_item_details_image = cart_item_details_image;
    }

    public String getExtra_item_details_name() {
        return extra_item_details_name;
    }

    public void setExtra_item_details_name(String extra_item_details_name) {
        this.extra_item_details_name = extra_item_details_name;
    }

    public String getExtra_item_details_price() {
        return extra_item_details_price;
    }

    public void setExtra_item_details_price(String extra_item_details_price) {
        this.extra_item_details_price = extra_item_details_price;
    }

    public String getExtra_item_details_item_id() {
        return extra_item_details_item_id;
    }

    public void setExtra_item_details_item_id(String extra_item_details_item_id) {
        this.extra_item_details_item_id = extra_item_details_item_id;
    }

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


}
