package me.pinger.pschedulers.scheduler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class ScheduleConfig {
    private ScheduleType type;
    
    // Pour INTERVAL
    private long intervalTicks;
    
    // Pour HOURLY
    private int minute;
    
    // Pour DAILY
    private LocalTime time;
    
    // Pour WEEKLY
    private Set<DayOfWeek> days;
    private LocalTime weeklyTime;
    
    public static ScheduleConfig createInterval(long ticks) {
        return builder()
            .type(ScheduleType.INTERVAL)
            .intervalTicks(ticks)
            .build();
    }
    
    public static ScheduleConfig createHourly(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
        return builder()
            .type(ScheduleType.HOURLY)
            .minute(minute)
            .build();
    }
    
    public static ScheduleConfig createDaily(LocalTime time) {
        return builder()
            .type(ScheduleType.DAILY)
            .time(time)
            .build();
    }
    
    public static ScheduleConfig createWeekly(Set<DayOfWeek> days, LocalTime time) {
        return builder()
            .type(ScheduleType.WEEKLY)
            .days(EnumSet.copyOf(days))
            .weeklyTime(time)
            .build();
    }
    
    public long getNextExecutionTime() {
        long currentTime = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();
        
        switch (type) {
            case INTERVAL:
                return currentTime + (intervalTicks * 50); // 1 tick = 50ms
                
            case HOURLY:
                LocalDateTime nextHour = now.withMinute(minute).withSecond(0).withNano(0);
                if (nextHour.isBefore(now)) {
                    nextHour = nextHour.plusHours(1);
                }
                return nextHour.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                
            case DAILY:
                LocalDateTime nextDay = now.with(time);
                if (nextDay.isBefore(now)) {
                    nextDay = nextDay.plusDays(1);
                }
                return nextDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                
            case WEEKLY:
                LocalDateTime nextWeekDay = now.with(weeklyTime);
                while (!days.contains(nextWeekDay.getDayOfWeek()) || nextWeekDay.isBefore(now)) {
                    nextWeekDay = nextWeekDay.plusDays(1);
                }
                return nextWeekDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                
            default:
                throw new IllegalStateException("Unknown schedule type: " + type);
        }
    }
} 