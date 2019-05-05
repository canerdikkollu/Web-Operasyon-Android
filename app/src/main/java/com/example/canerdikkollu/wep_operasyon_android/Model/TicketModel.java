package com.example.canerdikkollu.wep_operasyon_android.Model;

public class TicketModel {
    public int ID;
    public String TARIH;
    public String CREATOR;
    public int MUSTERI;
    public int HIZMET;
    public int PRIORITY;
    public String ASSIGNEDTO;
    public String WHOIS;
    public String LASTMODIFIEDDATE;
    public String ACIKLAMA;

    public TicketModel(int ID, String TARIH, String CREATOR, int MUSTERI, int HIZMET, int PRIORITY, String ASSIGNEDT, String WHOIS, String LASTMODIFIEDDATE, String ACIKLAMA) {
        this.ID = ID;
        this.TARIH = TARIH;
        this.CREATOR = CREATOR;
        this.MUSTERI = MUSTERI;
        this.HIZMET = HIZMET;
        this.PRIORITY = PRIORITY;
        this.ASSIGNEDTO = ASSIGNEDT;
        this.WHOIS = WHOIS;
        this.LASTMODIFIEDDATE = LASTMODIFIEDDATE;
        this.ACIKLAMA = ACIKLAMA;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTARIH() {
        return TARIH;
    }

    public void setTARIH(String TARIH) {
        this.TARIH = TARIH;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public int getMUSTERI() {
        return MUSTERI;
    }

    public void setMUSTERI(int MUSTERI) {
        this.MUSTERI = MUSTERI;
    }

    public int getHIZMET() {
        return HIZMET;
    }

    public void setHIZMET(int HIZMET) {
        this.HIZMET = HIZMET;
    }

    public int getPRIORITY() {
        return PRIORITY;
    }

    public void setPRIORITY(int PRIORITY) {
        this.PRIORITY = PRIORITY;
    }

    public String getASSIGNEDTO() {
        return ASSIGNEDTO;
    }

    public void setASSIGNEDTO(String ASSIGNEDT) {
        this.ASSIGNEDTO = ASSIGNEDT;
    }

    public String getWHOIS() {
        return WHOIS;
    }

    public void setWHOIS(String WHOIS) {
        this.WHOIS = WHOIS;
    }

    public String getLASTMODIFIEDDATE() {
        return LASTMODIFIEDDATE;
    }

    public void setLASTMODIFIEDDATE(String LASTMODIFIEDDATE) {
        this.LASTMODIFIEDDATE = LASTMODIFIEDDATE;
    }

    public String getACIKLAMA() {
        return ACIKLAMA;
    }

    public void setACIKLAMA(String ACIKLAMA) {
        this.ACIKLAMA = ACIKLAMA;
    }
}