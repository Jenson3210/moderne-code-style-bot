package com.yourorg;

public class ExistingCodeLibraryReferenceWithoutIssues {

    public boolean hasIssues(String s) {
        return AlreadyPresent.CONSTANT.equalsIgnoreCase(s);
    }
}
