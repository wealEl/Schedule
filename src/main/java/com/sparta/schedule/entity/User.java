package com.sparta.schedule.entity;

import com.sparta.schedule.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long userId;
    private String name;
    private long password;

    //클라이언트가 요청한 데이터를 담아 객체 생성
    public User(ScheduleRequestDto requestDto){
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
    }

}
