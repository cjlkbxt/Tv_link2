package com.kobe.lib_base;

import com.google.gson.Gson;

public class DataBean {
    public String manufacturer;
    public String model;
    public String cid;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
