package com.txd.library.picker.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.txd.library.picker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var pagerAdapter: FragmentStateAdapter

    private var fragments: MutableList<Fragment> = arrayListOf()

    private var tabTitles: MutableList<String> = arrayListOf()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVp()
        initListener()
    }

    private fun initListener() {
        binding.btnAdd.setOnClickListener {
            addItem()
        }
        binding.btnDel.setOnClickListener {
            delItem(2)
        }
    }

    private fun addItem() {
        tabTitles.add("title${fragments.size + 1}")
        fragments.add(PickerFragment.newInstance("title${fragments.size + 1}"))
        pagerAdapter.notifyItemRangeInserted(fragments.size - 1, 1)
        binding.vp.setCurrentItem(fragments.size - 1, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun delItem(delCount: Int = 1) {
        if (delCount > fragments.size) throw IndexOutOfBoundsException("delCount cannot be greater than allSize! delCount:$delCount allSize:${fragments.size}")
        val mCurItem = binding.vp.currentItem
        if (mCurItem == fragments.size - 1) {
            binding.vp.setCurrentItem(mCurItem - delCount, false)
        }
        tabTitles = tabTitles.dropLast(delCount).toMutableList()
        fragments = fragments.dropLast(delCount).toMutableList()
        binding.vp.post {
            pagerAdapter.notifyDataSetChanged()
        }
//        pagerAdapter.notifyItemRemoved(fragments.size - 1)
//        pagerAdapter.notifyItemRangeChanged(fragments.size - 1, fragments.size)
    }

    private fun initVp() {
        fragments.add(PickerFragment.newInstance("title1"))
        tabTitles.add("title1")

        pagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return tabTitles.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        setSupportsChangeAnimations(binding.vp)
        binding.vp.adapter = pagerAdapter

        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.vp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e(TAG, "onPageSelected: $position")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.e(TAG, "onPageScrolled: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.e(TAG, "onPageScrollStateChanged state: $state")
                if (state == 0) {
                    val position = binding.vp.currentItem
                    if (position != fragments.size - 1) {
                        delItem(fragments.size - 1 - position)
                    }
                }
            }
        })

        binding.vp.currentItem = 0
    }


}

fun setSupportsChangeAnimations(viewPager: ViewPager2, enable: Boolean = false) {
    for (i in 0 until viewPager.childCount) {
        val view: View = viewPager.getChildAt(i)
        if (view is RecyclerView) {
            val animator: RecyclerView.ItemAnimator? = view.itemAnimator
            if (animator != null) {
                (animator as SimpleItemAnimator).supportsChangeAnimations = enable
            }
            view.itemAnimator?.changeDuration = 0
            view.itemAnimator = null
            break
        }
    }
}