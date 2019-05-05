package com.example.canerdikkollu.wep_operasyon_android.Model;

public class DetailModel {

    public String TARIH;
    public String OPERATOR;
    public String ACIKLAMA;
    public String CCO;

    public DetailModel(String TARIH, String OPERATOR, String ACIKLAMA, String CCO) {
        this.TARIH = TARIH;
        this.OPERATOR = OPERATOR;
        this.ACIKLAMA = ACIKLAMA;
        this.CCO = CCO;
    }

    public String getTARIH() {
        return TARIH;
    }

    public void setTARIH(String TARIH) {
        this.TARIH = TARIH;
    }

    public String getOPERATOR() {
        return OPERATOR;
    }

    public void setOPERATOR(String OPERATOR) {
        this.OPERATOR = OPERATOR;
    }

    public String getACIKLAMA() {
        return ACIKLAMA;
    }

    public void setACIKLAMA(String ACIKLAMA) {
        this.ACIKLAMA = ACIKLAMA;
    }

    public String getCCO() {
        return CCO;
    }

    public void setCCO(String CCO) {
        this.CCO = CCO;
    }
}
