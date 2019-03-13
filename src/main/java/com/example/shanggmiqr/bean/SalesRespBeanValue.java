package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class SalesRespBeanValue {

    /**
     * errno : 1
     * errmsg : 行号[10]，物料编码[P03070001]，物料名称[收款机-商米T2 lite]，序列号或条码[184200072]出现负库存，请调整！
     */

    private String errno;
    private String errmsg;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
