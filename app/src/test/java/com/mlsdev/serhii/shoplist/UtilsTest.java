package com.mlsdev.serhii.shoplist;

import com.mlsdev.serhii.shoplist.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by serhii on 6/10/16.
 */
public class UtilsTest {
    private String expectedEmailResult;
    private String inputEmail;
    private String expectedDateResult;
    private long inputDate;

    @Before
    public void setUp(){
        expectedEmailResult = "petrosiuk@gmail,com";
        inputEmail = "petrosiuk@gmail.com";
        expectedDateResult = "10:42";
        inputDate = 1465544578;
    }

    @Test
    public void getFormattedDate_CorrectDataTest(){
        String result = Utils.getFormattedDate(inputDate);
        Assert.assertEquals(expectedDateResult, result);
    }

    @Test
    public void encodeEmail_CorrectDataTest() {
        String actualResult = Utils.encodeEmail(inputEmail);
        Assert.assertEquals(expectedEmailResult, actualResult);
    }

    @Test
    public void encodeEmail_IncorrectDataTest() {
        expectedEmailResult = ";ah;asg.asdgo";
        String actualResult = Utils.encodeEmail(inputEmail);
        Assert.assertNotEquals(expectedEmailResult, actualResult);
    }
}
