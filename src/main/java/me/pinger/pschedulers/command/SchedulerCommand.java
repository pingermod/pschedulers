package me.pinger.pschedulers.command;

import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.config.TaskConfig;
import me.pinger.pschedulers.manager.TaskManager;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.scheduler.ScheduleFormatter;
import me.pinger.pschedulers.scheduler.ScheduleType;
import me.pinger.pschedulers.scheduler.ScheduleValidator;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SchedulerCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "pschedulers.admin";
    private static final List<String> SUBCOMMANDS = Arrays.asList(
        "create", "remove", "start", "stop", "list", "reload"
    );
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    sendCreateHelp(sender);
                    return true;
                }
                handleCreate(sender, args);
                break;
            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /scheduler remove <id>", NamedTextColor.RED));
                    return true;
                }
                handleRemove(sender, args[1]);
                break;
            case "start":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /scheduler start <id>", NamedTextColor.RED));
                    return true;
                }
                handleStart(sender, args[1]);
                break;
            case "stop":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /scheduler stop <id>", NamedTextColor.RED));
                    return true;
                }
                handleStop(sender, args[1]);
                break;
            case "list":
                handleList(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void handleCreate(CommandSender sender, String[] args) {
        String id = args[1];
        
        try {
            ScheduleConfig config = null;
            String command;
            int argIndex;

            // Déterminer le type de planification
            ScheduleType type = ScheduleType.INTERVAL; // Par défaut
            if (args[2].toUpperCase().startsWith("TYPE:")) {
                String typeStr = args[2].substring(5);
                try {
                    type = ScheduleType.valueOf(typeStr.toUpperCase());
                    argIndex = 3;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid schedule type: " + typeStr, NamedTextColor.RED));
                    sendCreateHelp(sender);
                    return;
                }
            } else {
                // Format ancien : intervalle en ticks
                argIndex = 3;
                try {
                    long interval = Long.parseLong(args[2]);
                    config = ScheduleConfig.createInterval(interval);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid interval! Please provide a number or use TYPE:<type>.", NamedTextColor.RED));
                    return;
                }
            }

            // Créer la configuration selon le type si pas déjà créée
            if (config == null) {
                switch (type) {
                    case INTERVAL:
                        if (args.length < argIndex + 1) {
                            sender.sendMessage(Component.text("Usage: /scheduler create <id> TYPE:INTERVAL <ticks> <command>", NamedTextColor.RED));
                            return;
                        }
                        try {
                            long interval = Long.parseLong(args[argIndex]);
                            config = ScheduleConfig.createInterval(interval);
                            argIndex++;
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Component.text("Invalid interval! Please provide a number.", NamedTextColor.RED));
                            return;
                        }
                        break;

                    case HOURLY:
                        if (args.length < argIndex + 1) {
                            sender.sendMessage(Component.text("Usage: /scheduler create <id> TYPE:HOURLY <minute> <command>", NamedTextColor.RED));
                            return;
                        }
                        try {
                            int minute = Integer.parseInt(args[argIndex]);
                            config = ScheduleConfig.createHourly(minute);
                            argIndex++;
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Component.text("Invalid minute! Please provide a number between 0 and 59.", NamedTextColor.RED));
                            return;
                        }
                        break;

                    case DAILY:
                        if (args.length < argIndex + 1) {
                            sender.sendMessage(Component.text("Usage: /scheduler create <id> TYPE:DAILY <HH:mm> <command>", NamedTextColor.RED));
                            return;
                        }
                        try {
                            LocalTime time = LocalTime.parse(args[argIndex], TIME_FORMAT);
                            config = ScheduleConfig.createDaily(time);
                            argIndex++;
                        } catch (DateTimeParseException e) {
                            sender.sendMessage(Component.text("Invalid time! Please use format HH:mm (e.g. 14:30)", NamedTextColor.RED));
                            return;
                        }
                        break;

                    case WEEKLY:
                        if (args.length < argIndex + 2) {
                            sender.sendMessage(Component.text(
                                "Usage: /scheduler create <id> TYPE:WEEKLY <days> <HH:mm> <command>\n" +
                                "Days format: MON,TUE,WED,THU,FRI,SAT,SUN", NamedTextColor.RED));
                            return;
                        }
                        try {
                            Set<DayOfWeek> days = parseDays(args[argIndex]);
                            LocalTime time = LocalTime.parse(args[argIndex + 1], TIME_FORMAT);
                            config = ScheduleConfig.createWeekly(days, time);
                            argIndex += 2;
                        } catch (Exception e) {
                            sender.sendMessage(Component.text("Invalid days or time format! " + e.getMessage(), NamedTextColor.RED));
                            return;
                        }
                        break;

                    default:
                        sender.sendMessage(Component.text("Unsupported schedule type: " + type, NamedTextColor.RED));
                        return;
                }
            }

            // Valider la configuration
            if (config == null) {
                sender.sendMessage(Component.text("Failed to create schedule configuration", NamedTextColor.RED));
                return;
            }

            try {
                ScheduleValidator.validate(config);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Component.text("Invalid configuration: " + e.getMessage(), NamedTextColor.RED));
                return;
            }

            // Construire la commande à partir des arguments restants
            if (args.length <= argIndex) {
                sender.sendMessage(Component.text("Please specify a command to execute!", NamedTextColor.RED));
                return;
            }
            command = String.join(" ", Arrays.copyOfRange(args, argIndex, args.length));

            // Créer et démarrer la tâche
            Location location = sender instanceof Player ? ((Player) sender).getLocation() : null;
            ScheduledTask task = TaskManager.getInstance().createTask(id, command, config, location);
            task.start();
            TaskConfig.getInstance().saveConfig();

            // Envoyer un message de confirmation avec les détails
            sender.sendMessage(Component.text("Task created and started successfully!", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Schedule: " + ScheduleFormatter.getReadableSchedule(config), NamedTextColor.GRAY));
            sender.sendMessage(Component.text("Next execution: " + ScheduleFormatter.getNextExecutionText(config), NamedTextColor.GRAY));

        } catch (Exception e) {
            sender.sendMessage(Component.text("Error creating task: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
        }
    }

    private Set<DayOfWeek> parseDays(String daysStr) {
        Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        for (String day : daysStr.split(",")) {
            try {
                // Supporter les formats courts (MON) et longs (MONDAY)
                if (day.length() <= 3) {
                    day = expandDayAbbreviation(day.toUpperCase());
                }
                days.add(DayOfWeek.valueOf(day.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid day: " + day);
            }
        }
        if (days.isEmpty()) {
            throw new IllegalArgumentException("At least one day must be specified");
        }
        return days;
    }

    private String expandDayAbbreviation(String abbr) {
        switch (abbr.toUpperCase()) {
            case "MON": return "MONDAY";
            case "TUE": return "TUESDAY";
            case "WED": return "WEDNESDAY";
            case "THU": return "THURSDAY";
            case "FRI": return "FRIDAY";
            case "SAT": return "SATURDAY";
            case "SUN": return "SUNDAY";
            default: return abbr;
        }
    }

    private void handleList(CommandSender sender) {
        Collection<ScheduledTask> tasks = TaskManager.getInstance().getAllTasks();
        if (tasks.isEmpty()) {
            sender.sendMessage(Component.text("No tasks found!", NamedTextColor.YELLOW));
            return;
        }

        sender.sendMessage(Component.text("Scheduled Tasks:", NamedTextColor.GREEN));
        for (ScheduledTask task : tasks) {
            sender.sendMessage(Component.text("- " + task.getId() + 
                " (" + (task.isRunning() ? Component.text("Running", NamedTextColor.GREEN) : 
                Component.text("Stopped", NamedTextColor.RED)) + ")", NamedTextColor.GRAY));
            sender.sendMessage(Component.text("  Command: " + task.getCommand(), NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  Schedule: " + ScheduleFormatter.getReadableSchedule(task.getScheduleConfig()), NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  Next execution: " + ScheduleFormatter.getNextExecutionText(task.getScheduleConfig()), NamedTextColor.WHITE));
            if (task.getLocation() != null) {
                Location loc = task.getLocation();
                sender.sendMessage(Component.text("  Location: " + String.format("%.1f, %.1f, %.1f in %s", 
                    loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()), NamedTextColor.GRAY));
            }
        }
    }

    private void handleRemove(CommandSender sender, String id) {
        ScheduledTask task = TaskManager.getInstance().getTask(id);
        if (task == null) {
            sender.sendMessage(Component.text("Task not found!", NamedTextColor.RED));
            return;
        }

        TaskManager.getInstance().removeTask(id);
        TaskConfig.getInstance().saveConfig();
        sender.sendMessage(Component.text("Task removed successfully!", NamedTextColor.GREEN));
    }

    private void handleStart(CommandSender sender, String id) {
        ScheduledTask task = TaskManager.getInstance().getTask(id);
        if (task == null) {
            sender.sendMessage(Component.text("Task not found!", NamedTextColor.RED));
            return;
        }

        if (task.isRunning()) {
            sender.sendMessage(Component.text("Task is already running!", NamedTextColor.YELLOW));
            return;
        }

        task.start();
        TaskConfig.getInstance().saveConfig();
        sender.sendMessage(Component.text("Task started successfully!", NamedTextColor.GREEN));
        sender.sendMessage(Component.text("Next execution: " + ScheduleFormatter.getNextExecutionText(task.getScheduleConfig()), NamedTextColor.WHITE));
    }

    private void handleStop(CommandSender sender, String id) {
        ScheduledTask task = TaskManager.getInstance().getTask(id);
        if (task == null) {
            sender.sendMessage(Component.text("Task not found!", NamedTextColor.RED));
            return;
        }

        if (!task.isRunning()) {
            sender.sendMessage(Component.text("Task is not running!", NamedTextColor.YELLOW));
            return;
        }

        task.stop();
        TaskConfig.getInstance().saveConfig();
        sender.sendMessage(Component.text("Task stopped successfully!", NamedTextColor.GREEN));
    }

    private void handleReload(CommandSender sender) {
        TaskManager.getInstance().stopAllTasks();
        TaskConfig.getInstance().loadConfig();
        sender.sendMessage(Component.text("Configuration reloaded successfully!", NamedTextColor.GREEN));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("PSchedulers Commands:", NamedTextColor.GREEN));
        sender.sendMessage(Component.text("/scheduler create <id> [TYPE:<type>] <params> <command>", NamedTextColor.GRAY)
            .append(Component.text(" - Create a new task", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/scheduler remove <id>", NamedTextColor.GRAY)
            .append(Component.text(" - Remove a task", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/scheduler start <id>", NamedTextColor.GRAY)
            .append(Component.text(" - Start a task", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/scheduler stop <id>", NamedTextColor.GRAY)
            .append(Component.text(" - Stop a task", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/scheduler list", NamedTextColor.GRAY)
            .append(Component.text(" - List all tasks", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/scheduler reload", NamedTextColor.GRAY)
            .append(Component.text(" - Reload configuration", NamedTextColor.WHITE)));
        
        sendCreateHelp(sender);
    }

    private void sendCreateHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Task Creation Examples:", NamedTextColor.GREEN));
        sender.sendMessage(Component.text("1. Interval: ", NamedTextColor.GRAY)
            .append(Component.text("/scheduler create backup TYPE:INTERVAL 6000 backup world", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("2. Hourly: ", NamedTextColor.GRAY)
            .append(Component.text("/scheduler create broadcast TYPE:HOURLY 30 broadcast New hour soon!", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("3. Daily: ", NamedTextColor.GRAY)
            .append(Component.text("/scheduler create morning TYPE:DAILY 09:00 broadcast Good morning!", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("4. Weekly: ", NamedTextColor.GRAY)
            .append(Component.text("/scheduler create weekend TYPE:WEEKLY SAT,SUN 10:00 broadcast Weekend event!", NamedTextColor.WHITE)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return SUBCOMMANDS.stream()
                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                return Collections.emptyList(); // Libre pour l'ID
            }
            if (Arrays.asList("remove", "start", "stop").contains(args[0].toLowerCase())) {
                return TaskManager.getInstance().getAllTasks().stream()
                    .map(ScheduledTask::getId)
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("TYPE:INTERVAL");
            suggestions.add("TYPE:HOURLY");
            suggestions.add("TYPE:DAILY");
            suggestions.add("TYPE:WEEKLY");
            return suggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[2].toLowerCase()))
                .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
} 