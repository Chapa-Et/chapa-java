package com.github.amenski.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PostDataTest {

    @Test
    void test_correct_amount_parsing() {
        // given
        PostData postData = new PostData();
        postData.setAmount("100.0001");

        // when
        Map<String, Object> postDataAsMap = postData.getAsMap();

        // validate
        assertEquals("100.0001", postDataAsMap.get("amount"));
    }

    @Test
    void test_BigDecimal_from_Double_precision_issue() {
        // given
        PostData postData = new PostData();
        postData.setAmount(new BigDecimal(100.0001).toString());

        // when
        Map<String, Object> postDataAsMap = postData.getAsMap();

        // validate
        assertNotEquals("100.0001", postDataAsMap.get("amount"));
    }

}