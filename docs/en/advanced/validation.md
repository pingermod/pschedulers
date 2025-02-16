# Validation and Formatting

The plugin includes comprehensive validation and formatting features to ensure configuration correctness and maintain consistent data formats.

## Configuration Validation

### Task Validation
```yaml
tasks:
  example_task:
    type: DAILY          # Must be valid schedule type
    time: "12:00"        # Must be valid time format
    command: "say hello" # Must be non-empty
    enabled: true        # Must be boolean
```

### Template Validation
```yaml
templates:
  example_template:
    type: INTERVAL       # Must be valid schedule type
    interval: 6000       # Must be positive integer
    conditions:          # Must be valid condition format
      min_players: 1
```

## Data Types

### Time Formats
- 24-hour format: `"HH:mm"`
- Ticks: Positive integers
- Duration: Time units (s, m, h)

### Numbers
- Integers: No decimals
- Decimals: Up to 2 places
- Percentages: 0-100

### Booleans
- True: `true`, `yes`, `on`
- False: `false`, `no`, `off`

## Format Validation

### Time Validation
```yaml
tasks:
  daily_task:
    type: DAILY
    time: "14:30"      # Valid
    # time: "2:30 PM"  # Invalid
    # time: "25:00"    # Invalid
```

### Number Validation
```yaml
conditions:
  tps:
    min: 18.5         # Valid
    # min: "high"     # Invalid
  memory:
    max: 85.0         # Valid
    # max: -10       # Invalid
```

## Error Messages

### Format Errors
```
[ERROR] Invalid time format: "2:30 PM". Use 24-hour format (HH:mm)
[ERROR] Invalid number: "high". Must be a decimal number
[ERROR] Invalid boolean: "enabled". Use true/false
```

### Logic Errors
```
[ERROR] Min players (10) cannot be greater than max players (5)
[ERROR] Invalid time range: min (18000) > max (12000)
[ERROR] Duplicate task ID: "daily_backup"
```

## Best Practices

1. **Time Formats**
   - Use 24-hour format
   - Be consistent with timezone
   - Validate ranges

2. **Numbers**
   - Use appropriate ranges
   - Be consistent with units
   - Document thresholds

3. **Configuration**
   - Use clear structure
   - Comment complex values
   - Test before deployment

## Examples

### Valid Configurations

#### Time Formats
```yaml
tasks:
  morning:
    type: DAILY
    time: "09:00"
  
  evening:
    type: DAILY
    time: "21:30"
```

#### Number Ranges
```yaml
conditions:
  players:
    min: 5
    max: 50
  
  performance:
    tps:
      min: 18.5
    memory:
      max: 85.0
```

### Invalid Configurations

#### Time Format Errors
```yaml
tasks:
  invalid_time:
    type: DAILY
    time: "9:00 AM"    # Wrong format
  
  out_of_range:
    type: HOURLY
    minute: 75         # Invalid minute
```

#### Number Format Errors
```yaml
conditions:
  invalid_numbers:
    min_players: "few"  # Not a number
    max_players: -10    # Negative not allowed
```

## Validation Rules

### Task Rules
- ID must be unique
- Type must be valid
- Time/interval must be valid
- Commands must be non-empty

### Condition Rules
- Numbers must be in range
- Time must be valid format
- Weather types must be valid
- Worlds must exist

### Template Rules
- ID must be unique
- Properties must be valid
- Inheritance must be valid
- No circular references

## Troubleshooting

1. **Time Issues**
   - Check format (HH:mm)
   - Verify ranges
   - Consider timezone

2. **Number Issues**
   - Verify format
   - Check ranges
   - Validate units

3. **Logic Issues**
   - Check dependencies
   - Verify references
   - Test combinations

## Advanced Validation

### Custom Validators
```yaml
validation:
  custom_rules:
    - type: regex
      pattern: "^[a-z0-9_]+$"
      fields: [task_id, template_id]
    
    - type: range
      min: 0
      max: 24000
      fields: [time.min, time.max]
```

### Format Specifications
```yaml
formats:
  time: "HH:mm"
  duration: "\d+[smh]"
  identifier: "[a-z0-9_]+"
  command: "^[^/].*"
```

### Error Handling
```yaml
errors:
  invalid_time: "Invalid time format: {value}. Use HH:mm"
  invalid_number: "Invalid number: {value} for {field}"
  invalid_range: "{field} must be between {min} and {max}"
``` 