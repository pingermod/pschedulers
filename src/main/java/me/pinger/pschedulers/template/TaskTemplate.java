package me.pinger.pschedulers.template;

import lombok.Getter;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.scheduler.ScheduleType;
import me.pinger.pschedulers.scheduler.TaskConditions;
import org.bukkit.configuration.ConfigurationSection;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TaskTemplate {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Map<String, TaskTemplate> templates = new HashMap<>();

    private final String id;
    private final ScheduleType type;
    private final long intervalTicks;
    private final int minute;
    private final LocalTime time;
    private final Set<DayOfWeek> days;
    private final TaskConditions conditions;

    private TaskTemplate(String id, ConfigurationSection config) {
        this.id = id;
        this.type = ScheduleType.valueOf(config.getString("type", "INTERVAL"));
        this.intervalTicks = config.getLong("interval", 20L);
        this.minute = config.getInt("minute", 0);
        this.time = config.contains("time") ? LocalTime.parse(config.getString("time"), TIME_FORMAT) : null;
        
        List<String> dayStrings = config.getStringList("days");
        this.days = dayStrings.stream()
            .map(DayOfWeek::valueOf)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(DayOfWeek.class)));

        ConfigurationSection conditionsSection = config.getConfigurationSection("conditions");
        this.conditions = conditionsSection != null ? 
            TaskConditions.fromConfig(conditionsSection) : 
            TaskConditions.getDefault();
    }

    public static void loadTemplates(ConfigurationSection config) {
        templates.clear();
        ConfigurationSection templatesSection = config.getConfigurationSection("templates");
        if (templatesSection != null) {
            for (String id : templatesSection.getKeys(false)) {
                ConfigurationSection templateConfig = templatesSection.getConfigurationSection(id);
                if (templateConfig != null) {
                    templates.put(id, new TaskTemplate(id, templateConfig));
                }
            }
        }
    }

    public static TaskTemplate getTemplate(String id) {
        return templates.get(id);
    }

    public ScheduleConfig createConfig() {
        return ScheduleConfig.builder()
            .type(type)
            .intervalTicks(intervalTicks)
            .minute(minute)
            .time(time)
            .days(days)
            .conditions(conditions)
            .build();
    }
} 