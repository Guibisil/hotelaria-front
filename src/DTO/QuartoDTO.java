package DTO;

import com.google.gson.annotations.SerializedName;

public class QuartoDTO {

    private Long id;
    
    @SerializedName(value = "room_number", alternate = {"number"})
    private String number;
    
    private String status;
    
    @SerializedName("bed_count")
    private int bedCount;
    
    @SerializedName("base_daily_rate")
    private double baseDailyRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public double getBaseDailyRate() {
        return baseDailyRate;
    }

    public void setBaseDailyRate(double baseDailyRate) {
        this.baseDailyRate = baseDailyRate;
    }
}
