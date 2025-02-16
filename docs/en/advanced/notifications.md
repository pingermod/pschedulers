# Notification System

The notification system allows administrators and players to be informed of important plugin events through various channels.

## Configuration

Notifications are configured in the `notifications` section of `config.yml`:

```yaml
notifications:
  # Discord Configuration
  discord:
    enabled: false
    webhook_url: ""
    events: [START, STOP, FAILURE]

  # Console Configuration
  console:
    level: INFO              # Log level (INFO, WARNING, ERROR)

  # Player Configuration
  players:
    permission: "scheduler.notify"  # Permission to receive notifications
```

## Notification Channels

### Discord
- Uses Discord webhooks
- Real-time notifications
- Customizable format
- Secure webhook configuration

### Console
- Server console logs
- Different severity levels
- Standard log format
- Level-based filtering

### In-game Players
- Messages to players with permission
- Customizable format
- Color support
- Permission-based targeting

## Event Types

Available events are:

- `START`: Task start
- `STOP`: Task stop
- `FAILURE`: Task failure
- `SUCCESS`: Task success
- `RETRY`: Task retry
- `RELOAD`: Configuration reload

## Discord Configuration

### Basic Configuration
```yaml
discord:
  enabled: true
  webhook_url: "https://discord.com/api/webhooks/..."
  events: [START, STOP, FAILURE]
```

### Custom Events
```yaml
discord:
  enabled: true
  webhook_url: "https://discord.com/api/webhooks/..."
  events: 
    - START
    - STOP
    - FAILURE
    - SUCCESS
    - RELOAD
```

## Console Log Levels

Available levels are:
- `INFO`: General information
- `WARNING`: Warnings
- `ERROR`: Errors

Configuration:
```yaml
console:
  level: WARNING  # Shows only warnings and errors
```

## Player Notifications

### Basic Configuration
```yaml
players:
  permission: "scheduler.notify"
```

### Usage Example
```yaml
tasks:
  backup:
    command: "backup start"
    type: DAILY
    time: "04:00"
    notify_players: true  # Enables notifications for this task
```

## Best Practices

1. **Security**
   - Protect your Discord webhook
   - Limit notification permissions
   - Filter sensitive information

2. **Performance**
   - Avoid excessive Discord notifications
   - Use appropriate log level
   - Limit player notifications

3. **Organization**
   - Group notifications by importance
   - Define dedicated channels
   - Document important events

## Advanced Examples

### Complete Configuration
```yaml
notifications:
  discord:
    enabled: true
    webhook_url: "https://discord.com/api/webhooks/..."
    events: [START, STOP, FAILURE, SUCCESS]
    format: "[{task}] {message}"
  
  console:
    level: INFO
    format: "[PSchedulers] {level} - {message}"
  
  players:
    permission: "scheduler.notify"
    format: "&7[&bPS&7] {message}"
```

### Conditional Notifications
```yaml
tasks:
  critical_backup:
    command: "backup critical"
    type: DAILY
    time: "00:00"
    notifications:
      success:
        discord: true
        console: INFO
        players: true
      failure:
        discord: true
        console: ERROR
        players: true
```

## Troubleshooting

1. **Discord**
   - Verify webhook validity
   - Confirm webhook permissions
   - Test connection

2. **Console**
   - Check log level
   - Review complete logs
   - Enable debug mode

3. **Players**
   - Check permissions
   - Test different formats
   - Confirm receipt 