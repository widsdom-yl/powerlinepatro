package dczh.model;

public class UploadFileRetModel {
    public UploadFileRetModel(String u){
        url = u;
    }
    public String getUrl() {
        if (!url.contains("gz.aliyuns.vip")){
            return  "http://gz.aliyuns.vip/"+url;
        }
        return url;
    }
    public String getOriUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
}
