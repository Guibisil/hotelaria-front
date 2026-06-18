package DTO;

import com.google.gson.annotations.SerializedName;

public class ReservaDTO {

    private Long id;
    
    @SerializedName("guest_id")
    private Long guestId;
    
    private transient String guestName;
    private transient String roomNumber;
    private transient Double roomDailyRate;
    
    @SerializedName("room_id")
    private int roomId;
    
    @SerializedName("checkin_date")
    private String checkinDate;
    
    @SerializedName("checkout_date")
    private String checkoutDate;
    
    @SerializedName("total_amount")
    private Double totalAmount;
    
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Double getRoomDailyRate() {
        return roomDailyRate;
    }

    public void setRoomDailyRate(Double roomDailyRate) {
        this.roomDailyRate = roomDailyRate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
