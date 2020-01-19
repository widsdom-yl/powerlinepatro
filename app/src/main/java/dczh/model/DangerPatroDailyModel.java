package dczh.model;

import static dczh.Util.Config.rootUrl;

public class DangerPatroDailyModel {

    private int pid;
    private int id;
    private String gcj;
    private int icl;
    private String img;
    private String dte;
    private String aid;
    private String usr;
    private String wxy;
    public void setPid(int pid) {
        this.pid = pid;
    }
    public int getPid() {
        return pid;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setGcj(String gcj) {
        this.gcj = gcj;
    }
    public String getGcj() {
        return gcj;
    }

    public void setIcl(int icl) {
        this.icl = icl;
    }
    public int getIcl() {
        return icl;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {
        if (!img.contains(rootUrl)){
           return rootUrl + img;
        }
        return img;
    }

    public void setDte(String dte) {
        this.dte = dte;
    }
    public String getDte() {
        return dte;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
    public String getAid() {
        return aid;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }
    public String getUsr() {
        return usr;
    }

    public void setWxy(String wxy) {
        this.wxy = wxy;
    }
    public String getWxy() {
        return wxy;
    }

}

