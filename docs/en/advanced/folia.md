# Folia Support

PSchedulers provides native support for Folia, offering optimized task scheduling and region-based execution.

## Configuration

Enable Folia support in the main configuration:

```yaml
settings:
  folia:
    enabled: true
    region_tasks: true    # Enable region-based tasks
    async_execution: true # Enable async task execution
```

## Features

### Region-Based Tasks
- Tasks can be bound to specific regions
- Automatic region detection
- Performance optimization
- Thread-safe execution

### Async Task Execution
- Non-blocking task execution
- Improved server performance
- Safe command handling
- Automatic thread management

## Region Tasks

### Basic Configuration
```yaml
tasks:
  region_broadcast:
    type: INTERVAL
    interval: 6000
    region:
      world: "world"
      x: 100
      y: 64
      z: 100
      radius: 50
    command: "broadcast Hello to nearby players!"
```

### Dynamic Regions
```yaml
tasks:
  moving_effect:
    type: INTERVAL
    interval: 1200
    region:
      world: "world"
      follow_player: "admin"  # Region follows player
      radius: 30
    command: "effect give @p speed 30 2"
```

## Performance Optimization

### Thread Management
```yaml
settings:
  folia:
    threads:
      min: 1
      max: 4
      queue_size: 1000
```

### Region Caching
```yaml
settings:
  folia:
    region_cache:
      enabled: true
      size: 100
      expire: 300
```

## Best Practices

1. **Region Design**
   - Keep regions reasonably sized
   - Avoid overlapping regions
   - Consider server load

2. **Task Scheduling**
   - Use appropriate intervals
   - Balance task distribution
   - Monitor performance impact

3. **Command Execution**
   - Use region-aware commands
   - Handle async execution
   - Implement fallbacks

## Examples

### Static Region Task
```yaml
tasks:
  spawn_particles:
    type: INTERVAL
    interval: 100
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 20
    command: "particle end_rod ~ ~2 ~ 1 1 1 0.1 10"
```

### Dynamic Region Task
```yaml
tasks:
  player_aura:
    type: INTERVAL
    interval: 20
    region:
      follow_player: "VIP"
      radius: 10
      height: 5
    commands:
      - "particle portal ~ ~ ~ 0.5 0.5 0.5 0.1 5"
      - "effect give @p glowing 2 1"
```

## Advanced Features

### Region Groups
```yaml
regions:
  spawn_area:
    world: "world"
    points:
      - x: 100
        y: 64
        z: 100
      - x: -100
        y: 64
        z: -100
    tasks:
      - spawn_protection
      - welcome_message
      - particle_effects
```

### Conditional Regions
```yaml
tasks:
  event_area:
    type: INTERVAL
    interval: 6000
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 100
      conditions:
        min_players: 5
        time:
          min: 0
          max: 12000
    command: "broadcast Event active in this region!"
```

## Troubleshooting

1. **Region Issues**
   - Verify coordinates
   - Check world existence
   - Validate region size

2. **Performance Problems**
   - Monitor thread usage
   - Check region overlap
   - Optimize task frequency

3. **Command Failures**
   - Verify command syntax
   - Check permissions
   - Test async compatibility

## Migration Guide

### From Paper to Folia
1. Enable Folia support
2. Convert global tasks to regional
3. Update command syntax
4. Test thoroughly

### Configuration Updates
```yaml
# Old Paper config
tasks:
  global_task:
    type: INTERVAL
    interval: 6000
    command: "broadcast Hello"

# New Folia config
tasks:
  regional_task:
    type: INTERVAL
    interval: 6000
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 100
    command: "broadcast Hello"
```

## Limitations

- Some commands may not work async
- Region size impacts performance
- World load affects task execution
- Thread count affects memory usage 