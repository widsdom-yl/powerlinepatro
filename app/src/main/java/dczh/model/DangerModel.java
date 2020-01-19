package dczh.model;


import java.io.Serializable;

import dczh.powerlinepatro.R;

/**
 * Auto-generated: 2020-01-17 10:30:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class DangerModel implements Serializable {

    private String id;
    private String tpe;
    private String dgr;
    private String nme;
    private String dts;
    private String akt;
    private String cmt;
    private String fzr;
    private String dte;
    private String im1;
    private String im2;
    private String izs;
    private String ijs;
    private String ste;
    private String lot;
    private String lat;
    private String ara;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setTpe(String tpe) {
        this.tpe = tpe;
    }
    public String getTpe() {
        return tpe;
    }

    public void setDgr(String dgr) {
        this.dgr = dgr;
    }
    public String getDgr() {
        return dgr;
    }

    public void setNme(String nme) {
        this.nme = nme;
    }
    public String getNme() {
        return nme;
    }

    public void setDts(String dts) {
        this.dts = dts;
    }
    public String getDts() {
        return dts;
    }

    public void setAkt(String akt) {
        this.akt = akt;
    }
    public String getAkt() {
        return akt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }
    public String getCmt() {
        return cmt;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
    }
    public String getFzr() {
        return fzr;
    }

    public void setDte(String dte) {
        this.dte = dte;
    }
    public String getDte() {
        return dte;
    }

    public void setIm1(String im1) {
        this.im1 = im1;
    }
    public String getIm1() {
        return im1;
    }

    public void setIm2(String im2) {
        this.im2 = im2;
    }
    public String getIm2() {
        return im2;
    }

    public void setIzs(String izs) {
        this.izs = izs;
    }
    public String getIzs() {
        return izs;
    }

    public void setIjs(String ijs) {
        this.ijs = ijs;
    }
    public String getIjs() {
        return ijs;
    }

    public void setSte(String ste) {
        this.ste = ste;
    }
    public String getSte() {
        return ste;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
    public String getLot() {
        return lot;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }

    public void setAra(String ara) {
        this.ara = ara;
    }
    public String getAra() {
        return ara;
    }
    public int getLevelDescID() {
        try {
            int type = Integer.parseInt(tpe);
            switch (type){
                case 0:
                    return R.string.string_danger_level0;
                case 1:
                    return R.string.string_danger_level1;
                case 2:
                    return R.string.string_danger_level2;
                default:
                    return 0;
            }
        }
        catch (Exception e){
            return 0;
        }

    }
    public int getLevel(){
        try {
            int type = Integer.parseInt(tpe);
            return type;
        }
        catch (Exception e){
            return -1;
        }
    }

}