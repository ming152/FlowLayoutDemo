# FlowLayoutDemo
类似搜索历史记录排列
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/gif.gif)

    <com.example.ming.flowlayoutlibrary.FlowLayout
        android:id="@+id/flowLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:horizontal_space="12dp"
        app:vertical_space="8dp"
        app:adjustMod="none"
        app:gravity="left">
    </com.example.ming.flowlayoutlibrary.FlowLayout>

	horizontal_space : 表示子view之间横向间距
	vertical_space ：表示子view之间纵向间距
	adjustMod 表示子view排版模式，gravity 表示对齐方式

	adjustMod = “none"  gravity = "left"
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/left.png)

	adjustMod = “none"  gravity = "center"
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/center.png)

	adjustMod = “none"  gravity = "right"
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/right.png)

	adjustMod = adjust_space", 此时子view间间距平分横向空余空间，horizontal_space代表最小间距
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/item.png)

	adjustMod = adjust_item"  此时为保持间距不变，将横向剩余空间平均分配到子view中
![截图](https://github.com/ming152/FlowLayoutDemo/blob/master/screenshot/space.png)


另外可以使用代码设置上述属性：
	flowLayout.setAdjustMod();
	flowLayout.setGravity();
	flowLayout.setHorizontalSpace();
	flowLayout.setVerticalSpace();
	


