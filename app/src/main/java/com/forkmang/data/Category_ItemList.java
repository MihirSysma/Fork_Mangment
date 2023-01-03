package com.forkmang.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Category_ItemList implements Serializable {
    String id, category_id, name, price, image;
    public ArrayList<Extra_Topping> extra_toppingArrayList;

    public ArrayList<Extra_Topping> getExtra_toppingArrayList() {
        return extra_toppingArrayList;
    }

    public void setExtra_toppingArrayList(ArrayList<Extra_Topping> extra_toppingArrayList) {
        this.extra_toppingArrayList = extra_toppingArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
