package dczh.model;

import java.io.Serializable;

public class LineTowerModel implements Serializable {
    int pid;
    int id;
    String nme;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNme() {
        return nme;
    }

    public void setNme(String nme) {
        this.nme = nme;
    }

    public String getTpe() {
        return tpe;
    }

    public void setTpe(String tpe) {
        this.tpe = tpe;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    String tpe;//类型
    double lot;//
    double lat;//
//      "gttx": "123", //杆塔塔形
//              "gtgd": "123", //杆塔高度
//              "dhdj": "123", //大号侧方向档距
//              "ljdj": "123", //累加档距
    String gttx;//杆塔塔形
    String gtgd;//杆塔高度
    String dhdj;//大号侧方向档距
    String ljdj;//累加档距





}
