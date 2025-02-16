# PSchedulers - Advanced Scheduling Plugin for Paper/Folia

[![GitHub Release](https://img.shields.io/github/v/release/pingermod/pschedulers)](https://github.com/pingermod/pschedulers/releases/latest)
[![GitHub License](https://img.shields.io/github/license/pingermod/pschedulers)](https://github.com/pingermod/pschedulers/blob/main/LICENSE)
[![Discord](https://img.shields.io/discord/1164397902723022918)](https://discord.gg/3RmVzdMv3d)

PSchedulers is a powerful and flexible Minecraft plugin that allows automatic command execution based on various scheduling types. Compatible with both Paper and Folia, it offers advanced task scheduling with timezone and region support.

## 🌟 Features

- **Multiple Schedule Types**:
  - ⏱️ Regular interval (every X seconds/minutes/hours)
  - 🕐 Hourly (at a specific minute each hour)
  - 📅 Daily (at a specific time each day)
  - 📆 Weekly (on specific days at a specific time)

- **Execution Conditions**:
  - 👥 Minimum required players
  - 📊 Maximum allowed players
  - 🔄 Dynamic condition checking

- **Extended Compatibility**:
  - ✅ Full Paper support
  - ✅ Native Folia support with region management
  - ✅ Java 17+ compatible

- **Advanced Features**:
  - 🔄 Hot configuration reload
  - 📍 Location-based tasks support
  - 🔒 Integrated permission system
  - 💬 Fully customizable messages

## 📋 Commands

### Main Command: `/scheduler` (aliases: `/ps`, `/pschedulers`)

| Command | Permission | Description |
|---------|------------|-------------|
| `/scheduler create <id> [TYPE:<type>] <params> <command>` | `pschedulers.admin` | Creates a new scheduled task |
| `/scheduler remove <id>` | `pschedulers.admin` | Removes an existing task |
| `/scheduler start <id>` | `pschedulers.admin` | Starts a task |
| `/scheduler stop <id>` | `pschedulers.admin` | Stops a task |
| `/scheduler list` | `pschedulers.admin` | Lists all tasks |
| `/scheduler reload` | `pschedulers.admin` | Reloads the configuration |

## 🎯 Schedule Types

### 1. Interval (INTERVAL)
Executes a command at regular intervals.

```bash
/scheduler create backup TYPE:INTERVAL 6000 backup world
```
- `6000`: Interval in ticks (20 ticks = 1 second)
- Example: Every 5 minutes (6000 ticks)

### 2. Hourly (HOURLY)
Executes a command at a specific minute every hour.

```bash
/scheduler create broadcast TYPE:HOURLY 30 broadcast "Half hour mark!"
```
- `30`: Minute of the hour (0-59)
- Example: At the 30th minute of every hour

### 3. Daily (DAILY)
Executes a command at a specific time each day.

```bash
/scheduler create morning TYPE:DAILY 09:00 broadcast "Good morning everyone!"
```
- `09:00`: Time in HH:mm format (24h)
- Example: Every day at 9:00 AM

### 4. Weekly (WEEKLY)
Executes a command on specific days of the week at a specific time.

```bash
/scheduler create weekend TYPE:WEEKLY SAT,SUN 10:00 broadcast "Happy weekend!"
```
- `SAT,SUN`: Days of the week (MON,TUE,WED,THU,FRI,SAT,SUN)
- `10:00`: Time in HH:mm format (24h)
- Example: Saturdays and Sundays at 10:00 AM

## ⚙️ Configuration

### config.yml
```yaml
# PSchedulers Configuration
tasks:
  example_interval:
    command: "broadcast Hello World!"
    type: INTERVAL
    interval: 6000  # 5 minutes
    enabled: false
    conditions:
      min_players: 5  # Only execute when 5+ players are online

  example_hourly:
    command: "broadcast It's a new hour!"
    type: HOURLY
    minute: 0
    enabled: false
    conditions:
      max_players: 20  # Only execute when 20- players are online

  example_daily:
    command: "broadcast Good morning!"
    type: DAILY
    time: "09:00"
    enabled: false
    conditions:
      min_players: 1  # At least 1 player
      max_players: 50  # Maximum 50 players

  example_weekly:
    command: "broadcast Weekly maintenance!"
    type: WEEKLY
    days: [MONDAY, WEDNESDAY, FRIDAY]
    time: "20:00"
    enabled: false
```

## 🔧 Installation

1. Download the JAR file from the releases page
2. Place the file in your server's `plugins` folder
3. Restart the server or use a plugin manager
4. The default configuration will be generated automatically

## 📝 Permissions

- `pschedulers.admin`: Access to all plugin commands
  - Default: Operators only

## 🌐 Folia Compatibility

PSchedulers natively supports Folia with:
- Smart region management
- Performance optimization
- Thread-safe command execution

For Folia servers, tasks can be bound to specific regions by creating them at a player's location:
```bash
# As a player, the task will be bound to your position
/scheduler create region_task TYPE:INTERVAL 1200 broadcast "Regional message"
```

## 🤝 Support and Contributing

- 🐛 Bug Reports: [GitHub Issues](https://github.com/pingermod/pschedulers/issues)
- 💡 Suggestions: [GitHub Discussions](https://github.com/pingermod/pschedulers/discussions)
- 🔧 Contributions: [Pull Requests](https://github.com/pingermod/pschedulers/pulls) welcome

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details. 