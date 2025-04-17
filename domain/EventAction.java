package account.domain;

import account.infrastructure.CreateLogEventPublisher;

public enum EventAction {
    CREATE_USER,
    CHANGE_PASSWORD,
    ACCESS_DENIED,
    LOGIN_FAILED,
    GRANT_ROLE,
    REMOVE_ROLE,
    LOCK_USER,
    UNLOCK_USER,
    DELETE_USER,
    BRUTE_FORCE;

    public static String getSubject(EventAction action) {
        switch (action) {
            case CHANGE_PASSWORD:
            case ACCESS_DENIED:
            case LOGIN_FAILED:
            case BRUTE_FORCE:
                return "admin";
            default:
                return "user";

        }
    }
}
