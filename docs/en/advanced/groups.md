# Task Groups

Task groups allow you to organize and execute multiple tasks together, with shared conditions and specific execution modes.

## Configuration

Groups are defined in the `groups` section of `config.yml`:

```yaml
groups:
  backup:
    tasks: [world_backup, player_data_backup]
    sequential: true        # Sequential execution
    conditions:
      min_players: 0
      tps:
        min: 18.0
  
  morning:
    tasks: [clear_weather, heal_players, give_morning_bonus]
    parallel: true         # Parallel execution
```

## Execution Modes

### Sequential (`sequential: true`)
- Tasks are executed one after another
- Order is guaranteed
- A task is only executed if the previous one completed
- Useful for dependent operations

### Parallel (`parallel: true`)
- Tasks are executed simultaneously
- No order guarantee
- All tasks start at the same time
- Useful for independent operations

## Group Conditions

Conditions apply to the entire group:

```yaml
groups:
  event:
    tasks: [start_event, give_rewards, announce_winners]
    sequential: true
    conditions:
      min_players: 10
      max_players: 50
      weather:
        type: [CLEAR]
      time:
        min: 0
        max: 12000
```

## Error Handling

### Sequential Mode
- If a task fails, subsequent tasks are not executed
- Errors are logged
- The group is marked as failed

### Parallel Mode
- Errors in one task don't affect others
- Each task is independent
- The group continues even if some tasks fail

## Usage Examples

### Full Backup
```yaml
groups:
  full_backup:
    tasks: 
      - world_backup
      - player_data_backup
      - economy_backup
    sequential: true
    conditions:
      max_players: 5
      tps:
        min: 18.0
```

### Server Event
```yaml
groups:
  server_event:
    tasks:
      - announce_event
      - teleport_players
      - start_minigame
    parallel: true
    conditions:
      min_players: 10
```

### Daily Maintenance
```yaml
groups:
  daily_maintenance:
    tasks:
      - clear_logs
      - cleanup_temp
      - optimize_database
    sequential: true
    conditions:
      min_players: 0
```

## Best Practices

1. **Organization**
   - Group logically related tasks
   - Choose appropriate execution mode
   - Set appropriate conditions

2. **Performance**
   - Avoid too many parallel tasks
   - Monitor TPS impact
   - Schedule heavy groups during off-peak hours

3. **Maintenance**
   - Document dependencies
   - Test complete groups
   - Monitor error logs

## Limitations

- Maximum 10 tasks per group
- Groups cannot be nested
- A task can only belong to one active group 