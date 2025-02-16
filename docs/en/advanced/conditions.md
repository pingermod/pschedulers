# Execution Conditions

Execution conditions allow you to control when tasks are executed based on various server and world states.

## Configuration

Conditions are defined in the `conditions` section of tasks or templates:

```yaml
tasks:
  example_task:
    command: "broadcast Hello!"
    type: INTERVAL
    interval: 6000
    conditions:
      min_players: 5
      max_players: 50
      weather:
        type: [CLEAR, RAIN]
      time:
        min: 0
        max: 12000
      tps:
        min: 18.0
      memory:
        max: 85.0
```

## Available Conditions

### Player Count
```yaml
conditions:
  min_players: 5    # Minimum players online
  max_players: 50   # Maximum players online
```

### Weather
```yaml
conditions:
  weather:
    type: [CLEAR, RAIN]    # Allowed weather types
    world: "world"         # Specific world (optional)
```

### Time
```yaml
conditions:
  time:
    min: 0      # Minimum world time (0-24000)
    max: 12000  # Maximum world time (0-24000)
    world: "world"  # Specific world (optional)
```

### Performance
```yaml
conditions:
  tps:
    min: 18.0   # Minimum TPS
  memory:
    max: 85.0   # Maximum memory usage (%)
```

### Permissions
```yaml
conditions:
  permission: "myserver.vip"  # Required permission
  permission_count:
    min: 1    # Minimum players with permission
    max: 10   # Maximum players with permission
```

### World Player Count
```yaml
conditions:
  world_players:
    world: "world"
    min: 1     # Minimum players in world
    max: 20    # Maximum players in world
```

## Condition Groups

You can group conditions using AND/OR logic:

```yaml
conditions:
  AND:
    - min_players: 10
    - weather:
        type: [CLEAR]
    - time:
        min: 0
        max: 12000
  
  OR:
    - tps:
        min: 19.0
    - memory:
        max: 70.0
```

## Dynamic Conditions

### Time Ranges
```yaml
conditions:
  time:
    ranges:
      - min: 0     # Dawn
        max: 6000
      - min: 18000 # Dusk
        max: 24000
```

### Weather Sequences
```yaml
conditions:
  weather:
    sequence:
      - type: CLEAR
        duration: 6000
      - type: RAIN
        duration: 3000
```

## Advanced Usage

### Multiple World Conditions
```yaml
conditions:
  worlds:
    world:
      min_players: 5
      weather: [CLEAR]
    world_nether:
      max_players: 10
```

### Performance Thresholds
```yaml
conditions:
  performance:
    tps:
      min: 18.0
      duration: 300  # Must be stable for 300 ticks
    memory:
      max: 85.0
      duration: 600  # Must be stable for 600 ticks
```

### Permission Groups
```yaml
conditions:
  permissions:
    staff:
      permission: "staff.admin"
      min: 1
    vip:
      permission: "vip.user"
      min: 5
```

## Best Practices

1. **Performance**
   - Use appropriate thresholds
   - Avoid too frequent checks
   - Consider server capacity

2. **Reliability**
   - Set realistic conditions
   - Use condition groups wisely
   - Test condition combinations

3. **Maintenance**
   - Document complex conditions
   - Review conditions regularly
   - Monitor condition impact

## Examples

### Event Conditions
```yaml
tasks:
  special_event:
    type: DAILY
    time: "20:00"
    conditions:
      AND:
        - min_players: 20
        - weather:
            type: [CLEAR]
        - tps:
            min: 19.0
        - permission_count:
            permission: "event.participant"
            min: 10
```

### Maintenance Conditions
```yaml
tasks:
  maintenance:
    type: INTERVAL
    interval: 12000
    conditions:
      OR:
        - max_players: 5
        - AND:
          - tps:
              max: 15.0
          - memory:
              min: 90.0
```

## Troubleshooting

1. **Condition Not Met**
   - Check current server state
   - Verify condition values
   - Review condition logs

2. **Performance Impact**
   - Monitor check frequency
   - Optimize condition groups
   - Adjust thresholds

3. **Logic Issues**
   - Validate condition syntax
   - Check group logic
   - Test edge cases 