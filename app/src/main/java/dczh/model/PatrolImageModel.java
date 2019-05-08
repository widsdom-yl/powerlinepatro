package dczh.model;

public class PatrolImageModel {
    int pid;
    int id;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDte() {
        return dte;
    }

    public void setDte(String dte) {
        this.dte = dte;
    }

    String nme;
    String img;
    String dte;
}
