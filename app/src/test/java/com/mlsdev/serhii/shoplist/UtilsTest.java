package com.mlsdev.serhii.shoplist;

import com.mlsdev.serhii.shoplist.model.ItemUser;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    private static ItemUser itemOwner;
    private static ItemUser itemBuyer;
    private static List<ItemUser> usersInShop;
    private static String correctEmail = "petrosyuk@mlsdev.com";

    @BeforeClass
    public static void setUp() {
        userIsListOwner = new User(correctEmail, "Serhii", 0);
        userIsNotListOwner = new User("mlsdev@mlsdev.com", "MLSDev", 0);
        shoppingList = new ShoppingList(userIsListOwner.getName(), userIsListOwner.getEmail(), "List name");
        expectedDateResult = "10:42";
        inputDate = 1465544578;
        itemOwner = new ItemUser("Serhii", "petrosyuk@mlsdev.com");
        itemBuyer = new ItemUser("MLSDev", "mlsdev@mlsdev.com");
        usersInShop = new ArrayList<>();
    }

    @Before
    public void setUpEmails() {
        expectedEmailResult = "petrosiuk@gmail,com";
        inputEmail = "petrosiuk@gmail.com";
        usersInShop.add(itemBuyer);
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
        boolean actualResult = Utils.isUserListOrItemOwner(shoppingList.getOwnerEmail(), userIsListOwner.getEmail());
        Assert.assertTrue(actualResult);
    }

    @Test
    public void isUserListOwner_IncorrectData() {
        boolean actualResult = Utils.isUserListOrItemOwner(shoppingList.getOwnerEmail(), userIsNotListOwner.getEmail());
        Assert.assertFalse(actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isUserListOwner_IncorrectData_Null() {
        Utils.isUserListOrItemOwner(null, userIsNotListOwner.getEmail());
    }

    @Test
    public void getBuyerName_CurrentUserIsBuyerTest() {
        String actualResult = Utils.getBuyerName(itemOwner, itemOwner);
        Assert.assertNotNull(actualResult);
        Assert.assertEquals("You", actualResult);
    }

    @Test
    public void getBuyerName_CurrentUserIsNotBuyerTest() {
        String actualResult = Utils.getBuyerName(itemOwner, itemBuyer);
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(itemBuyer.getName(), actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBuyerName_IncorrectData_NullTest() {
        Utils.getBuyerName(null, null);
    }

    @Test
    public void isUserInShop_CorrectDataTest() {
        boolean actualResult = Utils.isUserInShop(usersInShop, itemBuyer.getEmail());
        Assert.assertTrue(actualResult);
    }

    @Test
    public void isUserInShop_CorrectDataUserIsNotInShopTest() {
        boolean actualResult = Utils.isUserInShop(usersInShop, "test@test.com");
        Assert.assertFalse(actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isUserInShop_IncorrectDataTest() {
        Utils.isUserInShop(null, "test@test.com");
    }
}
