package com.ymb.model;

public class RestaurantTable {

    private int tableId;
    private String tableNo;
    private int capacity;
    private String locationDesc;
    private boolean available = true; // computed at query time, not stored

    public RestaurantTable() {
    }

    public RestaurantTable(int tableId, String tableNo, int capacity, String locationDesc) {
        this.tableId = tableId;
        this.tableNo = tableNo;
        this.capacity = capacity;
        this.locationDesc = locationDesc;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
