package com.example.taskmanager.model.enums;

public enum SystemAuthorityName {
    CREATE_TASK("CREATE_TASK"),
    DELETE_TASK("DELETE_TASK"),
    CREATE_TEAM("CREATE_TEAM"),
    DELETE_TEAM("DELETE_TEAM"),
    CREATE_TEAM_RELATIONSHIP("CREATE_TEAM_RELATIONSHIP"),
    DELETE_TEAM_RELATIONSHIP("DELETE_TEAM_RELATIONSHIP");

    private final String value;

    SystemAuthorityName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public SystemAuthorityName getByValue(String value) {
        for (SystemAuthorityName authorityName : values()) {
            if (authorityName.value.equals(value)) {
                return authorityName;
            }
        }
        return null;
    }
}
