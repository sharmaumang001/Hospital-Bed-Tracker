package com.sharmaumang.hospital_bed_trackker.model;

public class bedModel
{
    private String hosName, available,total;
    private boolean expanded;
    private  String district,updatedate;

    public bedModel(String hosName, String available, String total, String district, String updatedate) {
        this.hosName = hosName;
        this.available = available;
        this.total = total;
        this.district = district;
        this.updatedate = updatedate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }
//    public bedModel(String hosName, String available, String total) {
//        this.hosName = hosName;
//        this.available = available;
//        this.total = total;
//    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "Details{" +
                "name='" + hosName + '\'' +
                ", available='" + available + '\'' +
                ", total='" + total + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
