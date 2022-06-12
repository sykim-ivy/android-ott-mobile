package com.ivytoy.setting

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.ivytoy.setting.ui.AdvancedDrawerLayout

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // 전체 영역
        val drawerLayout: AdvancedDrawerLayout = findViewById(R.id.drawer_layout)
        // 1depth Drawer 영역
        val navView: NavigationView = findViewById(R.id.nav_view)


        // AAC Navigation인데 메뉴가 모두 Navigation 이동이 아니라서 못씀 ㅠㅠ
        val navController: NavController = findNavController(R.id.nav_host_fragment) // 주 화면 영역에서의 NavController

        /**
         * asdjfla
        /**
         * asdjflas
         * /***asdfajsdlf*/
        */
         * /***asdfajsdlf*/
         */
        val e = "dsf"
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_noti, R.id.nav_child_setting, R.id.nav_channel_favor), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        // 2depth Drawer 영역
        val nav2DepthView: NavigationView = findViewById(R.id.nav_view_2depth)

        // 1depth Drawer 선택 리스너
        navView.setNavigationItemSelectedListener {
            val parent = navView.parent
            when (it.itemId) {
                R.id.nav_noti -> {
                    if(parent is AdvancedDrawerLayout) {
                        parent.closeDrawer(navView)
                    }
                }
                R.id.nav_child_setting -> {
                    if(parent is AdvancedDrawerLayout) {
                        parent.closeDrawer(navView)
                    }
                }
                R.id.nav_channel_setting -> {
                    Toast.makeText(this, "nav_channel_setting", Toast.LENGTH_LONG).show()
//                    nav2DepthView.visibility = View.VISIBLE
                    if(parent is AdvancedDrawerLayout) {
                        parent.closeDrawer(navView)
                        parent.openDrawer(nav2DepthView)
                    }
                }
                else -> {
                    Toast.makeText(this, "else id=${it.itemId}", Toast.LENGTH_LONG).show()
                }
            }
            val navResult = NavigationUI.onNavDestinationSelected(it, navController)
            Toast.makeText(this, "navResult=$navResult", Toast.LENGTH_LONG).show()
            true

        }

        // 2depth Drawer 선택 리스너
        nav2DepthView.setNavigationItemSelectedListener {
            val parent = nav2DepthView.parent
            when(it.itemId) {
                R.id.nav_2d_back -> {
                    if(parent is AdvancedDrawerLayout) {
                        parent.closeDrawer(nav2DepthView)
                    }
                }
                R.id.nav_channel_favor,
                R.id.nav_channel_info -> {
                    if(parent is AdvancedDrawerLayout) {
                        parent.closeDrawer(navView)
                        parent.closeDrawer(nav2DepthView)
                        val navResult = NavigationUI.onNavDestinationSelected(it, navController)
                        Toast.makeText(this, "[2Depth] navResult=$navResult", Toast.LENGTH_LONG).show()
                    }
                }
            }
            true
        }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}