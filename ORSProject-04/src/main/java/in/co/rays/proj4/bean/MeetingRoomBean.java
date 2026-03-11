package in.co.rays.proj4.bean;

public class MeetingRoomBean extends BaseBean {
    private String roomCode;
    private String roomName;
    private Integer capacity;
    private String roomStatus;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return roomCode + " - " + roomName + " (" + capacity + " persons)";
    }

    @Override
    public String toString() {
        return "MeetingRoomBean [roomCode=" + roomCode + ", roomName=" + roomName + ", capacity=" + capacity
                + ", roomStatus=" + roomStatus + "]";
    }
}