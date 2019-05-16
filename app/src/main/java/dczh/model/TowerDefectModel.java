package dczh.model;

import java.io.Serializable;

import dczh.MyApplication;
import dczh.powerlinepatro.R;

public class TowerDefectModel implements Serializable {
    int pid;
    int id;
    String nme;
    String usr;

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

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public String getDte() {
        return dte;
    }

    public void setDte(String dte) {
        this.dte = dte;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    Object img;
    String dte;

    /*
    *   "pid": 1000000,
            "id": 1,
            "nme": "缺陷内容",
            "img": "http://gz.aliyuns.vip/uld/usr/20190404/20190404202534716_1000000001.jpg",
            "dte": "2019-04-04 12:12:12"
            */


    public String getStausDesc() {
        if (status){
            return MyApplication.getInstance().getString(R.string.string_tower_defect_status_handled);

        }
        else {
            return MyApplication.getInstance().getString(R.string.string_tower_defect_status_unhandled);
        }
    }


    boolean status;
}
