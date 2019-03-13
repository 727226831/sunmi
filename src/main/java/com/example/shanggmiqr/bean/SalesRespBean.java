package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class SalesRespBean {

    /**
     * name : anyType
     * namespace : http://www.w3.org/2001/XMLSchema
     * value : {"errno":"1","errmsg":"行号[10]，物料编码[P03070001]，物料名称[收款机-商米T2 lite]，序列号或条码[184200072]出现负库存，请调整！"}
     * attributes : []
     */

    private String name;
    private String namespace;
    private String value;
    private List<?> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<?> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<?> attributes) {
        this.attributes = attributes;
    }
}
