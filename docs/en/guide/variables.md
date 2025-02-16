# Custom Variables

Custom variables allow you to define reusable values in your tasks. They are particularly useful for centralizing messages, rewards, or command sequences.

## Configuration

Variables are defined in the `variables` section of `config.yml`:

```yaml
variables:
  # Custom messages
  custom_messages:
    morning: "Good morning everyone!"
    evening: "Good evening everyone!"
    
  # Predefined rewards
  rewards:
    daily: "diamond 1"
    weekly: "diamond_block 1"
    
  # Command sequences
  commands:
    restart_warning:
      - "broadcast Server restarting in {time}"
      - "title @a title Restarting"
```

## Variable Types

### Custom Messages
- Defined in `custom_messages`
- Used to centralize frequently used messages
- Accessed via `{message.message_name}`

### Rewards
- Defined in `rewards`
- Used to standardize rewards
- Accessed via `{reward.reward_name}`

### Command Sequences
- Defined in `commands`
- Allow executing multiple commands in sequence
- Useful for complex actions

## Usage in Tasks

### Messages
```yaml
tasks:
  morning_message:
    command: "broadcast {message.morning}"
    type: DAILY
    time: "09:00"
```

### Rewards
```yaml
tasks:
  daily_reward:
    command: "give @a {reward.daily}"
    type: DAILY
    time: "12:00"
```

### Command Sequences
```yaml
tasks:
  server_restart:
    commands: "{commands.restart_warning}"
    type: DAILY
    time: "04:00"
```

## System Variables

In addition to custom variables, several system variables are available:

- `%players%`: Number of online players
- `%time%`: Current time
- `%date%`: Current date
- `%world%`: Current world
- `%tps%`: Server TPS

## Best Practices

1. **Organization**
   - Group variables by category
   - Use descriptive names
   - Comment their usage

2. **Maintenance**
   - Centralize frequent messages
   - Standardize rewards
   - Document complex sequences

3. **Performance**
   - Avoid overly long sequences
   - Prefer variables over repetition
   - Use system variables moderately

## Advanced Examples

### Message Templates
```yaml
custom_messages:
  welcome: "&aWelcome to the server, {player}!"
  goodbye: "&eGoodbye, {player}! See you soon!"
  announcement: "&6[Announcement] &f{message}"
```

### Reward Packages
```yaml
rewards:
  starter_kit:
    - "iron_sword 1"
    - "iron_armor_set 1"
    - "bread 32"
  
  vip_package:
    - "diamond_sword 1"
    - "diamond_armor_set 1"
    - "golden_apple 16"
```

### Complex Command Sequences
```yaml
commands:
  event_start:
    - "broadcast Event starting in 1 minute!"
    - "title @a title Event Starting"
    - "effect give @a speed 30 2"
    - "tp @a {event.location}"
  
  maintenance:
    - "save-all"
    - "broadcast Maintenance mode enabled"
    - "whitelist on"
    - "kick @a Server entering maintenance mode"
```

## Variable Formatting

### Text Colors
- Use `&` color codes
- Support for hex colors
- Format codes for styles

### Placeholders
- Player-specific: `{player}`
- Time-based: `{time}`, `{date}`
- Custom: `{variable_name}`

### Nested Variables
```yaml
custom_messages:
  event_reward: "You received {reward.event_prize}!"
  daily_summary: "Players: %players%, TPS: %tps%"
```

## Troubleshooting

1. **Variable Not Found**
   - Check variable name spelling
   - Verify section placement
   - Check access syntax

2. **Formatting Issues**
   - Verify color codes
   - Check placeholder syntax
   - Validate YAML format

3. **Performance**
   - Monitor command sequence length
   - Check variable resolution time
   - Optimize complex sequences 