package me.pinger.pschedulers.notification;

public enum NotificationEvent {
    START,      // Quand une tâche démarre
    STOP,       // Quand une tâche s'arrête
    FAILURE,    // Quand une tâche échoue
    SUCCESS,    // Quand une tâche réussit
    RETRY,      // Quand une tâche est réessayée
    RELOAD      // Quand la configuration est rechargée
} 