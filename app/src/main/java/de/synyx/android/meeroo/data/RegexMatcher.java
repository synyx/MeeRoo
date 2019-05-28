package de.synyx.android.meeroo.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class RegexMatcher {

    private RegexMatcher() {

        // just utils
    }

    static Integer getFirstNumberInBracketsFromString(String string) {

        final String regex = "(?<=[\\(])\\d+(?=[\\)])";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        matcher.find();

        try {
            return Integer.valueOf(matcher.group(0));
        } catch (NumberFormatException | IllegalStateException exception) {
            return null;
        }
    }


    static String removeNumberInBracketsFromString(String string) {

        final String regex = "(?<!\\()[^()]+(?![\\)])";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        StringBuilder cleanText = new StringBuilder();

        while (matcher.find()) {
            cleanText.append(matcher.group());
        }

        return cleanText.toString();
    }
}
