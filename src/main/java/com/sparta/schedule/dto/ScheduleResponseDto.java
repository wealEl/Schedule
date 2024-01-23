package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private String name;
    private String title;
    private String content;
    private String createdDate;

    public ScheduleResponseDto(User user, Schedule schedule) {
        this.name = user.getName();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.createdDate = schedule.getCreatedDate();

    }
}
