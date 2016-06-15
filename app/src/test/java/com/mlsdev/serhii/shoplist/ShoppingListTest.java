package com.mlsdev.serhii.shoplist;

import com.mlsdev.serhii.shoplist.model.ItemUser;
import com.mlsdev.serhii.shoplist.model.ShoppingList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serhii on 6/15/16.
 */
public class ShoppingListTest {
    private ShoppingList shoppingList;
    private List<ItemUser> shoppers;
    private ItemUser currentUser;
    private ItemUser shopperOne;
    private ItemUser shopperTwo;
    private ItemUser shopperThree;
    private String oneUserIsShopperText = " is shopping";
    private String twoOrMoreUsersAreShoppersText = " are shopping";
    private String moreThanTwoUsersAreShopping = "More than two users are shopping";
    private String and = " and ";

    @Before
    public void setUp() {
        shoppers = new ArrayList<>();
        shoppingList = new ShoppingList();
        currentUser = new ItemUser("Serhii", "current.user@gmail.com");
        shopperOne = new ItemUser("Shopper One", "shopper.one@gmail.com");
        shopperTwo = new ItemUser("Shopper Two", "shopper.two@gmail.com");
        shopperThree = new ItemUser("Shopper Three", "shopper.three@gmail.com");
        shoppingList.setUsersInShop(shoppers);
    }

    @Test
    public void getShoppers_JustCurrentUserIsShopperTest() {
        shoppingList.addUserToShop(currentUser.getEmail(), currentUser.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertTrue(actualResult.equals(""));
    }

    @Test
    public void getShoppers_NoOneIsShopperTest() {
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertTrue(actualResult.equals(""));
    }

    @Test
    public void getShoppers_JustOneUserIsShopperTest() {
        String expectedResult = shopperOne.getName() + oneUserIsShopperText;
        shoppingList.addUserToShop(shopperOne.getEmail(), shopperOne.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShoppers_TwoUsersAreShopperAndCurrentUserIsNotTest() {
        String expectedResult = shopperOne.getName() + and + shopperTwo.getName() + twoOrMoreUsersAreShoppersText;
        shoppingList.addUserToShop(shopperOne.getEmail(), shopperOne.getName());
        shoppingList.addUserToShop(shopperTwo.getEmail(), shopperTwo.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShoppers_TwoUsersAreShopperAndCurrentUserIsInTest() {
        String expectedResult = shopperOne.getName() + oneUserIsShopperText;
        shoppingList.addUserToShop(shopperOne.getEmail(), shopperOne.getName());
        shoppingList.addUserToShop(currentUser.getEmail(), currentUser.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShoppers_MoreThanTwoUsersAreShopperAndCurrentUserIsNotTest() {
        String expectedResult = moreThanTwoUsersAreShopping;
        shoppingList.addUserToShop(shopperOne.getEmail(), shopperOne.getName());
        shoppingList.addUserToShop(shopperTwo.getEmail(), shopperTwo.getName());
        shoppingList.addUserToShop(shopperThree.getEmail(), shopperThree.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShoppers_MoreThanThreeUsersAreShopperAndCurrentUserIsInTest() {
        String expectedResult = moreThanTwoUsersAreShopping;
        shoppingList.addUserToShop(shopperOne.getEmail(), shopperOne.getName());
        shoppingList.addUserToShop(shopperTwo.getEmail(), shopperTwo.getName());
        shoppingList.addUserToShop(shopperThree.getEmail(), shopperThree.getName());
        shoppingList.addUserToShop(currentUser.getEmail(), currentUser.getName());
        String actualResult = shoppingList.getShoppers(currentUser.getEmail());
        Assert.assertEquals(expectedResult, actualResult);
    }

}
