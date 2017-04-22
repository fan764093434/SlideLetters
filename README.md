# 自定义滑动字母检索控件
##### 虽然网上有很多例子，但是我还是想自己动手写一下，而且很多都不能满足我的开发需求，就当练手了
#### 效果图
![默认属性](https://github.com/fan764093434/SlideLetters/blob/master/image/S70422-094733.jpg) ![添加自定义属性后](https://github.com/fan764093434/SlideLetters/blob/master/image/S70422-100822.jpg)
#### 使用步骤
添加依赖
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```
dependencies {
    compile 'com.github.fan764093434:SlideLetters:1.0.1'
}
```
#### 使用代码
第一步
```
<com.fsw.slidelettersmenu.SlideLettersMenu
    android:id="@+id/slide_bar"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:layout_alignParentRight="true"
    android:layout_gravity="right"
    android:layout_marginRight="5dp"
    SlideLettersMenu:textColor="#ff00ff"
    SlideLettersMenu:textSize="18sp" />
```
SlideLettersMenu:textColor="#ff00ff"<br/>
SlideLettersMenu:textSize=18sp<br/>
以上连句代码是自定义属性，分别用来控制控件中字体的大小和字母的颜色，字体的大小到达一定程度，则不会在增加<br/>
第二步<br/>
```
//滑动监听
slideBar.setOnSlideListener(new SlideLettersMenu.OnSlideListener() {
    @Override
    public void onStartSlide(int index) {//开始滑动

    }

    @Override
    public void onSliding(int index) {//正在滑动

    }

    @Override
    public void onStopSlide(String index) {//滑动结束
        int position = adapter.getPositionForSection(index.charAt(0));
        if (position != -1) {
            //指定ListVIEW展示选择的下标数据
            listView.setSelection(position);
        }
    }
});
```
详细使用代码看example中的示例