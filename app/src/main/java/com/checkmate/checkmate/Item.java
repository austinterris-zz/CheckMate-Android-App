package com.checkmate.checkmate;


/**
 * Created by Brady on 10/27/2016.
 */

public class Item {
    private String m_itemName;
    private int m_HFID;
    private int m_UHFID;
    private String m_price;

    public Item(int m_HFID, String m_itemName, String m_price, int m_UHFID) {
        this.m_HFID = m_HFID;
        this.m_itemName = m_itemName;
        this.m_price = m_price;
        this.m_UHFID = m_UHFID;
    }

    public int getM_HFID() {
        return m_HFID;
    }

    public void setM_HFID(int m_HFID) {
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

    public int getM_UHFID() {
        return m_UHFID;
    }

    public void setM_UHFID(int m_UHFID) {
        this.m_UHFID = m_UHFID;
    }
}
