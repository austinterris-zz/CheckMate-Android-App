package com.checkmate.checkmate;


/**
 * Created by Brady on 10/27/2016.
 */

public class Item {
    private String m_itemName;
    private String  m_HFID;
    private String m_UHFID;
    private String m_price;

    public Item(String m_HFID, String m_itemName, String m_price, String m_UHFID) {
        this.m_HFID = m_HFID;
        this.m_itemName = m_itemName;
        this.m_price = m_price;
        this.m_UHFID = m_UHFID;
    }

    public String getM_HFID() {
        return m_HFID;
    }

    public void setM_HFID(String m_HFID) {
        this.m_HFID = m_HFID;
    }

    public String getM_itemName() {
        return m_itemName;
    }

    public void setM_itemName(String m_itemName) {
        this.m_itemName = m_itemName;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public String getM_UHFID() {
        return m_UHFID;
    }

    public void setM_UHFID(String m_UHFID) {
        this.m_UHFID = m_UHFID;
    }
}
