package com.mlsdev.serhii.shoplist;

import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by serhii on 6/10/16.
 */
public class UtilsTest {
    private static String expectedEmailResult;
    private static String inputEmail;
    private static String expectedDateResult;
    private static User userIsListOwner;
    private static User userIsNotListOwner;
    private static ShoppingList shoppingList;
    private static long inputDate;

    @BeforeClass
    public static void setUp() {
        userIsListOwner = new User("petrosyuk@mlsdev.com", "Serhii", 0);
        userIsNotListOwner = new User("mlsdev@mlsdev.com", "MLSDev", 0);
        shoppingList = new ShoppingList(userIsListOwner.getName(), userIsListOwner.getEmail(), "List name");
        expectedDateResult = "10:42";
        inputDate = 1465544578;
    }

    @Before
    public void setUpEmails(){
        expectedEmailResult = "petrosiuk@gmail,com";
        inputEmail = "petrosiuk@gmail.com";
    }

    @Test
    public void getFormattedDate_CorrectDataTest() {
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

    @Test
    public void isUserListOwner_CorrectData() {
        boolean actualResult = Utils.isUserListOwner(shoppingList.getOwnerEmail(), userIsListOwner.getEmail());
        Assert.assertTrue(actualResult);
    }

    @Test
    public void isUserListOwner_IncorrectData() {
        boolean actualResult = Utils.isUserListOwner(shoppingList.getOwnerEmail(), userIsNotListOwner.getEmail());
        Assert.assertFalse(actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isUserListOwner_IncorrectData_Null() {
        Utils.isUserListOwner(null, userIsNotListOwner.getEmail());
    }

}
