package com.example.liyun.testlayout.Bean;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * 模块实体类
 */
public class ModelBean {
    public String modelName;
    public String type;
    public List<ItemBean> itemDataList;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ItemBean> getItemDataList() {
        return itemDataList;
    }

    public void setItemDataList(List<ItemBean> itemDataList) {
        this.itemDataList = itemDataList;
    }
}
