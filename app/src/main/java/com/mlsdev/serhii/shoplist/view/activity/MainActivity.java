package com.mlsdev.serhii.shoplist.view.activity;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityMainBinding;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.fragment.BaseFragment;
import com.mlsdev.serhii.shoplist.view.fragment.MealListsFragment;
import com.mlsdev.serhii.shoplist.view.fragment.ShopListsFragment;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;
import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

public class MainActivity extends BaseActivity implements ILogOutView {
    public static final int SHOP_LISTS_FRAGMENT = 0;
    public static final int MEAL_LISTS_FRAGMENT = 1;
    private ActivityMainBinding binding;
    private PageAdapter pageAdapter;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new ViewModel());
        initViewPager();
        initToolBar(false);
        viewModel = new AccountViewModel(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        viewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out_menu_item:
                logUserOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logUserOut() {
        viewModel.logUserOut();
        finish();
    }

    private void initViewPager() {
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(pageAdapter);
        binding.pager.setOffscreenPageLimit(2);
        binding.tabBar.setupWithViewPager(binding.pager);
    }

    @Override
    public void userLoggedOut() {
        Intent startSignInIntent = new Intent(this, SignInActivity.class);
        startActivity(startSignInIntent);
        finish();
    }

    public class PageAdapter extends FragmentStatePagerAdapter {
        public BaseFragment shoppingFragment;
        public BaseFragment mealFragment;

        public PageAdapter(FragmentManager fm) {
            super(fm);
            shoppingFragment = ShopListsFragment.getNewInstance(null);
            mealFragment = MealListsFragment.getNewInstance(null);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case MEAL_LISTS_FRAGMENT:
                    return mealFragment;
                case SHOP_LISTS_FRAGMENT:
                default:
                    return shoppingFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case MEAL_LISTS_FRAGMENT:
                    return getString(R.string.pager_title_meals);
                case SHOP_LISTS_FRAGMENT:
                default:
                    return getString(R.string.pager_title_shopping_lists);
            }
        }
    }

    public class ViewModel extends BaseObservable {

        public void onAddItemButtomClicked(View view) {
            Bundle args = new Bundle();
            args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.DIALOG_TYPE_CREATING);
            ShoppingListDialogFragment dialogFragment = ShoppingListDialogFragment.getNewInstance(args);

            dialogFragment.setTargetFragment(
                    binding.pager.getCurrentItem() == SHOP_LISTS_FRAGMENT ? pageAdapter.shoppingFragment : pageAdapter.mealFragment,
                    ShoppingListDialogFragment.REQUEST_CODE);
            dialogFragment.show(getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
        }

    }
}
