package cn.ponfee.scheduler.supervisor.test.common.util;

import com.google.common.collect.Lists;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AssertJ
 *
 * @author Ponfee
 */
public class AssertJTest {

    @Test
    public void test1() {
        List<String> stringList = Lists.newArrayList("A", "B", "C");
        assertThat(stringList).contains("A"); //true
        assertThat(stringList).doesNotContain("D"); //true
        assertThat(stringList).containsExactly("A", "B", "C"); //true
    }
    @Test
    public void test2() {
        List<String> stringList = Lists.newArrayList("A", "B", "C");
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(stringList).contains("A"); //true
        softly.assertThat(stringList).containsExactly("A", "B", "C"); //true
        // Don't forget to call SoftAssertions global verification!
        softly.assertAll();
    }

}
