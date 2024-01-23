package com.sparta.schedule.controller;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//HTTP 요청에 대한 응답을 생성
@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedule")
    //@RequestBody :  HTTP 요청의 본문(body)을 메소드의 파라미터로 전달받을 때 사용
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // RequestDto -> Entity(객체 데이터)
        User user = new User(requestDto);
        Schedule schedule = new Schedule(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
        //데이터베이스에 새로운 행을 추가하면서 자동으로 생성된 키 값

        String User_sql = "INSERT INTO user (name, password) VALUES (?, ?)";
        //SQL 쿼리에 포함된 ?에 해당하는 부분에 매개변수 값을 전달
        //con : 데이터베이스와의 연결을 나타내는 Connection 객체
        //PreparedStatement : JDBC(Java Database Connectivity)에서 제공하는 인터페이스, 미리 컴파일된(precompiled) SQL 문을 나타내는 객체
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(User_sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setLong(2, user.getPassword());
                    return preparedStatement;
                },
                keyHolder);

        long id = keyHolder.getKey().longValue();
        user.setUserId(id);

        String Schedule_sql = "INSERT INTO schedule( title, content, created_date, user_id) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(Schedule_sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getTitle());
                    preparedStatement.setString(2, schedule.getContent());
                    preparedStatement.setString(3, schedule.getCreatedDate());
                    preparedStatement.setLong(4, user.getUserId());
                    return preparedStatement;
                },
                keyHolder);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(user, schedule);
        return scheduleResponseDto;
    }

    @GetMapping("/schedules")
    ///api/book/{bookId}
   public List<ScheduleResponseDto> getSchedule() {
        // DB 조회
        String sql = "SELECT user.name, schedule.title, schedule.content, schedule.created_date FROM user JOIN schedule ON user.user_id = schedule.schedule_id;";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                String name = rs.getString("name");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createdDate = rs.getString("created_date");
                return new ScheduleResponseDto(name, title, content, createdDate);
            }
        });

    }
}
