package me.pinger.pschedulers.scheduler;

public enum ScheduleType {
    INTERVAL,  // Toutes les X ticks (comportement actuel)
    HOURLY,    // Toutes les heures à une minute spécifique
    DAILY,     // Tous les jours à une heure spécifique
    WEEKLY;    // Chaque semaine à des jours spécifiques
} 