package de.synyx.android.meeroo.data;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class RegexMatcherTest {

    @Test
    public void getFirstNumberInBracketsFromString() {

        assertThat(RegexMatcher.getFirstNumberInBracketsFromString("Room(1)")).isEqualTo(1);
        assertThat(RegexMatcher.getFirstNumberInBracketsFromString("Room(10)")).isEqualTo(10);
        assertThat(RegexMatcher.getFirstNumberInBracketsFromString("Room (10)")).isEqualTo(10);
        assertThat(RegexMatcher.getFirstNumberInBracketsFromString("Room / Lounge (10)")).isEqualTo(10);
        assertThat(RegexMatcher.getFirstNumberInBracketsFromString("Room!\"§$%&/(ad)=) (10)")).isEqualTo(10);
    }


    @Test
    public void removeNumberInBracketsFromString() {

        assertThat(RegexMatcher.removeNumberInBracketsFromString("Room(1)")).isEqualTo("Room");
        assertThat(RegexMatcher.removeNumberInBracketsFromString("Room (10)")).isEqualTo("Room ");
        assertThat(RegexMatcher.removeNumberInBracketsFromString("Room / Lounge (10)")).isEqualTo("Room / Lounge ");
        assertThat(RegexMatcher.removeNumberInBracketsFromString("Room!\"§$%&/ad= (10)")).isEqualTo(
            "Room!\"§$%&/ad= ");
        assertThat(RegexMatcher.removeNumberInBracketsFromString("Room (10) Extra")).isEqualTo("Room  Extra");
    }
}
