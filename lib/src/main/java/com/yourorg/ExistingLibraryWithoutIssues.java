package com.yourorg;

import static com.yourorg.AlreadyPresentInLibrary.CONSTANT;

public class ExistingLibraryWithoutIssues {

    public boolean hasIssues(String s) {
        return CONSTANT.equalsIgnoreCase(s);
    }
}
