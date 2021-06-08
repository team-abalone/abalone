package com.teamabalone.abalone.Client.Responses;

import java.util.UUID;

public class SurrenderResponse extends BaseResponse {
    protected UUID surrenderedFrom;

    public UUID getSurrenderedFrom(){
        return this.surrenderedFrom;
    }
}
