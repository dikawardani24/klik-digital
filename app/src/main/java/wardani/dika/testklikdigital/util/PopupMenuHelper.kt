package wardani.dika.testklikdigital.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper

@Suppress("MemberVisibilityCanBePrivate", "unused")
object PopupMenuHelper {

    data class Menu(var id: Int, var title: String)

    interface PopMenuItemSelectedListener {
        fun onSelectedMenuId(selectedMenu: Menu)
    }

    private fun createPopupMenuNoIcon(
        context: Context,
        anchor: View,
        popMenuItemSelectedListener: PopMenuItemSelectedListener
    ): PopupMenu {
        return PopupMenu(context, anchor).apply {
            setOnMenuItemClickListener {
                popMenuItemSelectedListener.onSelectedMenuId(
                    Menu(
                        it.itemId,
                        it.title.toString()
                    )
                )
                true
            }
        }
    }

    fun showPopupMenuNoIcon(
        context: Context,
        anchor: View,
        menus: List<Menu>,
        popMenuItemSelectedListener: PopMenuItemSelectedListener
    ) {
        createPopupMenuNoIcon(
            context,
            anchor,
            popMenuItemSelectedListener
        ).run {
            for (i in menus.indices) {
                menu.add( 1, menus[i].id, i, menus[i].title)
            }
            show()
        }
    }

    fun showPopupMenuNoIcon(
        context: Context,
        anchor: View,
        menuTitles: Array<String>,
        popMenuItemSelectedListener: PopMenuItemSelectedListener
    ) {
        val menus = ArrayList<Menu>()
        for (i in menuTitles.indices) {
            menus.add(
                Menu(
                    i,
                    menuTitles[i]
                )
            )
        }

        showPopupMenuNoIcon(
            context,
            anchor,
            menus,
            popMenuItemSelectedListener
        )
    }

    fun showPopupMenuNoIcon(
        context: Context,
        anchor: View,
        resMenuId: Int,
        popMenuItemSelectedListener: PopMenuItemSelectedListener
    ) {
        createPopupMenuNoIcon(
            context,
            anchor,
            popMenuItemSelectedListener
        ).run {
            menuInflater.inflate(resMenuId, menu)
            show()
        }
    }

    @SuppressLint("RestrictedApi")
    fun showPopupMenuWithIcon(
        context: Context,
        anchor: View,
        resMenuId: Int,
        popMenuItemSelectedListener: PopMenuItemSelectedListener
    ) {
        val menuBuilder = MenuBuilder(context)
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                item.let {
                    popMenuItemSelectedListener.onSelectedMenuId(
                        Menu(
                            item.itemId,
                            item.title.toString()
                        )
                    )
                }
                return true
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })

        val inflater = MenuInflater(context)
        inflater.inflate(resMenuId, menuBuilder)

        val optionsMenu = MenuPopupHelper(context, menuBuilder, anchor)
        optionsMenu.setForceShowIcon(true)
        optionsMenu.show()
    }
}