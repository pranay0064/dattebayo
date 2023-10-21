package com.dattebayo.dattebayo.api;

import lombok.Data;

@Data
public class UsersProfileResponse {
    private String email;

    private String username;

    private String leetcodeProfileName;

    private boolean visibility;

    private LeetCodeStatsResponse leetcodeStats;
}
