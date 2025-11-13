package com.yourorg;

import static com.yourorg.AlreadyPresent.CONSTANT;

public class ExistingCodeWithIssues {

    public boolean hasIssues(String s) {
        boolean b = s.equalsIgnoreCase(CONSTANT);

        return b;
    }
}
