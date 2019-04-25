package dczh.model;

import java.io.Serializable;

import dczh.MyApplication;
import dczh.powerlinepatro.R;

public class TowerDefectModel implements Serializable {
    String tower;

    public TowerDefectModel(String tower, String towerDefectTime, String towerDefectWorker, boolean status) {
        this.tower = tower;
        this.towerDefectTime = towerDefectTime;
        this.towerDefectWorker = towerDefectWorker;
        this.status = status;
    }

    public String getTower() {
        return tower;
    }

    public void setTower(String tower) {
        this.tower = tower;
    }

    public String getTowerDefectTime() {
        return towerDefectTime;
    }

    public void setTowerDefectTime(String towerDefectTime) {
        this.towerDefectTime = towerDefectTime;
    }

    public String getTowerDefectWorker() {
        return towerDefectWorker;
    }

    public void setTowerDefectWorker(String towerDefectWorker) {
        this.towerDefectWorker = towerDefectWorker;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getStausDesc() {
        if (status){
            return MyApplication.getInstance().getString(R.string.string_tower_defect_status_handled);

        }
        else {
            return MyApplication.getInstance().getString(R.string.string_tower_defect_status_unhandled);
        }
    }

    String towerDefectTime;
    String towerDefectWorker;
    boolean status;
}
