package com.korit.board.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private int userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private int enabled;
}
