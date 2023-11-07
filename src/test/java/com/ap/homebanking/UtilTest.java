package com.ap.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ap.homebanking.utils.Util.getRandomNumber;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilTest {
    @Test
    public void createRandom(){
        int numberTest = getRandomNumber(0, 9);
        assertThat(numberTest, is(greaterThan(0)));
    }

    @Test
    public void checkRandomType(){
        int typeTest = getRandomNumber(0, 9);
        assertThat(typeTest, isA(Integer.class));
    }
}
