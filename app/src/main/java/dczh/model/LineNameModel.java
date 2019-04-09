package dczh.model;

import java.io.Serializable;

public class LineNameModel implements Serializable {
    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public LineNameModel(String lineName) {
        this.lineName = lineName;
    }

    String lineName;
}
