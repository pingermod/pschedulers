# Regional Tasks

Regional tasks allow you to execute commands in specific areas of your worlds, with support for both static and dynamic regions.

## Configuration

Regional tasks are configured in the task definition:

```yaml
tasks:
  spawn_effects:
    type: INTERVAL
    interval: 1200
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 50
    command: "particle end_rod ~ ~2 ~ 1 1 1 0.1 10"
```

## Region Types

### Static Regions
```yaml
region:
  world: "world"
  x: 100
  y: 64
  z: 100
  radius: 30      # Circular region
  # OR
  length: 50      # Rectangular region
  width: 50
  height: 10
```

### Dynamic Regions
```yaml
region:
  world: "world"
  follow_player: "admin"  # Region follows player
  radius: 20
  height: 5
```

## Region Shapes

### Circular
```yaml
region:
  world: "world"
  x: 0
  y: 64
  z: 0
  radius: 50
```

### Rectangular
```yaml
region:
  world: "world"
  x: 0
  y: 64
  z: 0
  length: 100
  width: 50
  height: 20
```

### Polygonal
```yaml
region:
  world: "world"
  points:
    - x: 0
      y: 64
      z: 0
    - x: 100
      y: 64
      z: 0
    - x: 50
      y: 64
      z: 100
```

## Region Features

### Player Tracking
```yaml
region:
  follow_player: "VIP"
  radius: 30
  update_interval: 100  # Ticks between updates
```

### Height Control
```yaml
region:
  world: "world"
  x: 0
  z: 0
  y_min: 0    # Minimum Y level
  y_max: 256  # Maximum Y level
  radius: 50
```

### Multiple Regions
```yaml
regions:
  - world: "world"
    x: 0
    y: 64
    z: 0
    radius: 50
  
  - world: "world_nether"
    x: 100
    y: 64
    z: 100
    radius: 30
```

## Advanced Usage

### Region Groups
```yaml
region_groups:
  spawn_area:
    regions:
      - world: "world"
        x: 0
        y: 64
        z: 0
        radius: 100
      
      - world: "world"
        x: 200
        y: 64
        z: 200
        radius: 50
    
    tasks:
      - particle_effects
      - player_buffs
      - area_protection
```

### Conditional Regions
```yaml
tasks:
  event_zone:
    type: INTERVAL
    interval: 6000
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 100
      conditions:
        active_hours:
          - start: "18:00"
            end: "22:00"
        min_players: 5
```

## Best Practices

1. **Performance**
   - Limit region size
   - Optimize update frequency
   - Balance region count

2. **Design**
   - Use appropriate shapes
   - Consider overlap
   - Plan region placement

3. **Maintenance**
   - Document region purposes
   - Monitor performance
   - Regular testing

## Examples

### Event Arena
```yaml
tasks:
  pvp_arena:
    type: INTERVAL
    interval: 100
    region:
      world: "world"
      x: 1000
      y: 64
      z: 1000
      radius: 50
      height: 30
    commands:
      - "effect give @p[gamemode=survival] strength 5 1"
      - "particle crit ~ ~1 ~ 0.5 0.5 0.5 0.1 10"
```

### Safe Zone
```yaml
tasks:
  spawn_protection:
    type: INTERVAL
    interval: 20
    region:
      world: "world"
      x: 0
      y: 64
      z: 0
      radius: 100
    commands:
      - "effect give @p resistance 2 255"
      - "effect give @p regeneration 2 1"
```

## Troubleshooting

1. **Region Issues**
   - Check coordinates
   - Verify world names
   - Test boundaries

2. **Performance Problems**
   - Monitor server load
   - Check region size
   - Optimize intervals

3. **Command Execution**
   - Test selectors
   - Verify permissions
   - Check syntax

## Command Targeting

### Player Selection
```yaml
commands:
  - "@p[distance=..30]"      # Nearest player
  - "@a[distance=..50]"      # All players
  - "@r[distance=..20]"      # Random player
  - "@e[type=player,dx=10]"  # Players in box
```

### Location Relative
```yaml
commands:
  - "particle end_rod ~ ~2 ~"        # Above player
  - "setblock ~5 ~ ~5 stone"         # Offset from player
  - "fill ~-5 ~-1 ~-5 ~5 ~3 ~5 air" # Area around player
```

## Region Management

### Creating Regions
```yaml
/scheduler region create <id> [radius]
/scheduler region set <id> <x> <y> <z>
/scheduler region addpoint <id> <x> <y> <z>
```

### Modifying Regions
```yaml
/scheduler region modify <id> radius <value>
/scheduler region modify <id> height <value>
/scheduler region move <id> <x> <y> <z>
```

### Removing Regions
```yaml
/scheduler region remove <id>
/scheduler region clear <id>
/scheduler region delete <id>
``` 