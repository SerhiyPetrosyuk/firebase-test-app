package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityMainBinding;
import com.mlsdev.serhii.shoplist.view.fragment.AddItemDialogFragment;
import com.mlsdev.serhii.shoplist.view.fragment.MealListsFragment;
import com.mlsdev.serhii.shoplist.view.fragment.ShopListsFragment;

public class MainActivity extends AppCompatActivity {
    public static final int SHOP_LISTS_FRAGMENT = 0;
    public static final int MEAL_LISTS_FRAGMENT = 1;
    private ActivityMainBinding binding;
    private ShopListsFragment shopListsFragment;
    private MealListsFragment mealListsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new ViewModel());
        initViewPager();
    }

    private void initViewPager() {
        shopListsFragment = ShopListsFragment.getNewInstance(null);
        mealListsFragment = MealListsFragment.getNewInstance(null);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(pageAdapter);
        binding.pager.setOffscreenPageLimit(2);
        binding.tabBar.setupWithViewPager(binding.pager);
    }

    public class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case MEAL_LISTS_FRAGMENT:
                    return mealListsFragment;
                case SHOP_LISTS_FRAGMENT:
                default:
                    return shopListsFragment;
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
            AddItemDialogFragment dialogFragment = AddItemDialogFragment.getNewInstance(null);
            dialogFragment.setTargetFragment(
                    binding.pager.getCurrentItem() == SHOP_LISTS_FRAGMENT ? shopListsFragment : mealListsFragment,
                    AddItemDialogFragment.REQUEST_CODE);
            dialogFragment.show(getSupportFragmentManager(), AddItemDialogFragment.class.getSimpleName());
        }

    }
}
