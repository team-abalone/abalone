package com.teamabalone.abalone.Client.Responses;

import java.util.UUID;

public class PlayerLeftResponse extends BaseResponse {
        protected UUID userId;

        public UUID getUserId(){
            return userId;
        }
}
