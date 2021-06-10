package com.teamabalone.abalone.Client.Requests;

public class LeaveRoomRequest extends BaseRequest{
    public LeaveRoomRequest(){
        this.commandCode=RequestCommandCodes.LEAVE_ROOM.getValue();
    }
}
