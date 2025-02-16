# Task Templates

Task templates allow you to define reusable task configurations that can be inherited and customized by individual tasks.

## Configuration

Templates are defined in the `templates` section of `config.yml`:

```yaml
templates:
  # Basic announcement template
  announcement:
    type: INTERVAL
    interval: 12000  # 10 minutes
    conditions:
      min_players: 1
    
  # Daily reward template
  daily_reward:
    type: DAILY
    time: "12:00"
    conditions:
      min_players: 5
      weather:
        type: [CLEAR, RAIN]
```

## Template Properties

### Basic Properties
- `type`: Schedule type (INTERVAL, HOURLY, DAILY, WEEKLY)
- `enabled`: Default enabled state
- `conditions`: Execution conditions
- `notifications`: Notification settings

### Schedule-specific Properties
- INTERVAL: `interval`
- HOURLY: `minute`
- DAILY: `time`
- WEEKLY: `days`, `time`

## Using Templates

### Basic Usage
```yaml
tasks:
  welcome_message:
    template: announcement
    command: "broadcast Welcome to the server!"
```

### Overriding Properties
```yaml
tasks:
  special_reward:
    template: daily_reward
    time: "18:00"  # Overrides template time
    command: "give @a diamond 1"
```

## Template Inheritance

### Single Inheritance
```yaml
templates:
  base:
    type: INTERVAL
    interval: 6000
    
  extended:
    template: base
    conditions:
      min_players: 5
```

### Property Merging
- Simple properties are overwritten
- Lists are concatenated
- Maps are merged recursively

## Advanced Features

### Condition Templates
```yaml
templates:
  peak_hours:
    conditions:
      min_players: 20
      tps:
        min: 18.0
      time:
        min: 36000  # 10:00
        max: 72000  # 20:00
```

### Notification Templates
```yaml
templates:
  critical:
    notifications:
      discord: true
      console: ERROR
      players: true
```

### Regional Templates
```yaml
templates:
  region_specific:
    region:
      world: "world"
      x: 100
      y: 64
      z: 100
      radius: 50
```

## Best Practices

1. **Organization**
   - Create templates for common patterns
   - Use descriptive template names
   - Document template purposes

2. **Inheritance**
   - Keep inheritance chains short
   - Be explicit about overrides
   - Document inherited properties

3. **Maintenance**
   - Review template usage regularly
   - Update templates centrally
   - Test template changes

## Examples

### Announcement System
```yaml
templates:
  base_announcement:
    type: INTERVAL
    interval: 12000
    conditions:
      min_players: 1
    
tasks:
  rules:
    template: base_announcement
    interval: 24000  # Every 20 minutes
    command: "broadcast Remember to follow the rules!"
    
  tips:
    template: base_announcement
    command: "broadcast Tip: Use /help for commands!"
```

### Event System
```yaml
templates:
  event_base:
    type: WEEKLY
    days: [SATURDAY, SUNDAY]
    time: "20:00"
    conditions:
      min_players: 10
    notifications:
      discord: true
      players: true
      
tasks:
  pvp_event:
    template: event_base
    commands:
      - "broadcast PvP Event starting!"
      - "tp @a {event.pvp.location}"
      
  parkour_event:
    template: event_base
    time: "15:00"  # Different time
    commands:
      - "broadcast Parkour Event starting!"
      - "tp @a {event.parkour.location}"
```

## Troubleshooting

1. **Template Not Found**
   - Check template name
   - Verify template section
   - Check inheritance chain

2. **Property Override Issues**
   - Check property names
   - Verify value types
   - Review inheritance order

3. **Condition Problems**
   - Validate condition syntax
   - Check condition values
   - Test condition combinations 