package com.yourorg;

import static com.yourorg.AlreadyPresentInLibrary.CONSTANT;

public class CommentMeLibraryUsage {

    public boolean hasIssues(String s) {
        boolean b = s.equalsIgnoreCase(CONSTANT);

        return b;
    }
}
