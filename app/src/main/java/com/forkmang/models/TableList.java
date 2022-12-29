package com.forkmang.models;

import java.io.Serializable;

public class TableList implements Serializable {
    String id,restaurant_id, table_no, number_of_person, type,floor_id,area_id,price,status_id;
    String table_descr, table_rule, table_drescode, table_ocassion;

    public String getTable_descr() {
        return table_descr;
    }

    public void setTable_descr(String table_descr) {
        this.table_descr = table_descr;
    }

    public String getTable_rule() {
        return table_rule;
    }

    public void setTable_rule(String table_rule) {
        this.table_rule = table_rule;
    }

    public String getTable_drescode() {
        return table_drescode;
    }

    public void setTable_drescode(String table_drescode) {
        this.table_drescode = table_drescode;
    }

    public String getTable_ocassion() {
        return table_ocassion;
    }

    public void setTable_ocassion(String table_ocassion) {
        this.table_ocassion = table_ocassion;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getNumber_of_person() {
        return number_of_person;
    }

    public void setNumber_of_person(String number_of_person) {
        this.number_of_person = number_of_person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
