package com.sparta.schedule.entity;

import com.sparta.schedule.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Schedule {
    private long scheduleId;
    private String title;
    private String content;
    private String createdDate;
    private long userId;

    public Schedule(ScheduleRequestDto requestDto){

        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.createdDate = requestDto.getCreatedDate();
    }
}
