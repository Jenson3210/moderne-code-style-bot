package com.yourorg;

import static com.yourorg.AlreadyPresent.CONSTANT;

public class ExistingCodeWithoutIssues {

    public boolean hasIssues(String s) {
        return CONSTANT.equalsIgnoreCase(s);
    }
}
