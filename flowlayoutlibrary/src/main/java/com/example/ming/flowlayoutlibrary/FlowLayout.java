package com.example.ming.flowlayoutlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2018/3/8.
 */

public class FlowLayout extends ViewGroup {
    public static final int ADJUST_NONE = 0;
    public static final int ADJUST_SPACE = 1;
    public static final int ADJUST_ITEM = 2;
    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_RIGHT = 2;

    private int adjustMod;
    private int gravity;
    private int horizontalSpace = 8;
    private int verticalSpace = 4;
    private int maxWidth;
    private List<Line> lines;
    private Line currentLine;
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr,
                0);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if(attr == R.styleable.FlowLayout_adjustMod){
                adjustMod = a.getInt(attr, 0);
            }else if(attr == R.styleable.FlowLayout_horizontal_space){
                horizontalSpace = a.getDimensionPixelOffset(attr, 0);
            }else if(attr == R.styleable.FlowLayout_vertical_space){
                verticalSpace = a.getDimensionPixelOffset(attr, 0);
            }else if(attr == R.styleable.FlowLayout_gravity){
                gravity = a.getInt(attr, 0);
            }
        }
        a.recycle();

        lines = new ArrayList<>();
    }

    public void setAdjustMod(int adjustMod) {
        this.adjustMod = adjustMod;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lines.clear();
        currentLine = null;
        maxWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int width = 0;
        int height = 0;
        int childCount = getChildCount();
        for(int i=0; i<childCount; i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if(currentLine == null){
                currentLine = new Line();
                lines.add(currentLine);
            }
            if(currentLine.canAddView(child)){
                currentLine.addView(child);
            }else {
                currentLine = new Line();
                lines.add(currentLine);
                currentLine.addView(child);
            }
        }
        //获取最长行的宽度
        for(Line line : lines){
            width = width > line.getWidth() ? width : line.getWidth();
            height += line.getHeight();
        }
        //所有行加起来高度
        if(lines.size() > 0){
            height += verticalSpace * (lines.size() - 1);
        }
        //占用的最大宽度
        width = width + getPaddingLeft() + getPaddingRight();
        //占用的高度
        height = height + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(getWidthSize(width, widthMeasureSpec),
                getHeightSize(height, heightMeasureSpec));
    }

    /**
     * 获取FlowLayout的计算宽度
     * @param usedWidth 已经占用的宽度
     * @param widthMeasureSpec 父类允许的宽度
     * @return 计算宽度
     */
    private int getWidthSize(int usedWidth, int widthMeasureSpec){
        int result = usedWidth;
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMod){
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(usedWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = usedWidth;
                break;
        }
        return result;
    }

    /**
     * 获取FlowLayout的计算高度
     * @param usedHeight 已经占用的高度
     * @param heightMeasureSpec 父类允许的高度
     * @return 计算高度
     */
    private int getHeightSize(int usedHeight, int heightMeasureSpec){
        int result = usedHeight;
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMod){
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(usedHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = usedHeight;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (adjustMod){
            case ADJUST_NONE:
                layoutAdjustNone(l, t, r, b);
                break;
            case ADJUST_ITEM:
                layoutAdjustItem(l, t, r, b);
                break;
            case ADJUST_SPACE:
                layoutAdjustSpace(l, t, r, b);
                break;
            default:
                layoutAdjustNone(l, t, r, b);
                break;
        }
    }

    /**
     * 对FlowLayout空间中横向剩余控件，不进行填充
     * @param l FlowLayout相对于父控件的布局位置
     * @param t FlowLayout相对于父控件的布局位置
     * @param r FlowLayout相对于父控件的布局位置
     * @param b FlowLayout相对于父控件的布局位置
     */
    private void layoutAdjustNone(int l, int t, int r, int b){
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int totalWidth = r - l - paddingLeft - paddingRight;
        int layoutLeft = 0;
        int layoutTop = paddingTop;
        for(Line line : lines) {
            List<View> viewList = line.getViewList();
            if (viewList.size() <= 0) {
                continue;
            }
            int lineHeight = line.getHeight();
            if (gravity == GRAVITY_RIGHT) {
                //水平右对齐
                layoutLeft = totalWidth - line.getWidth() + paddingLeft;
            } else if (gravity == GRAVITY_CENTER) {
                //水平居中
                layoutLeft = (totalWidth - line.getWidth()) / 2 + paddingLeft;
            } else {
                //水平左对齐
                layoutLeft = paddingLeft;
            }
            //循环指定每一行中子View位置
            for(int i=0; i<viewList.size(); i++) {
                View child = viewList.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int left = layoutLeft;
                //空间在每一行中垂直居中
                int top = layoutTop + (lineHeight - childHeight) / 2;
                int right = left + childWidth;
                int bottom = top + childHeight;
                child.layout(left, top, right, bottom);
                layoutLeft = layoutLeft + childWidth + horizontalSpace;
            }
            layoutTop = layoutTop + lineHeight + verticalSpace;
        }
    }

    /**
     * 子View宽度等量扩大，使子View在水平方向铺满
     * @param l FlowLayout相对于父控件的布局位置
     * @param t FlowLayout相对于父控件的布局位置
     * @param r FlowLayout相对于父控件的布局位置
     * @param b FlowLayout相对于父控件的布局位置
     */
    private void layoutAdjustItem(int l, int t, int r, int b){
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int totalWidth = r - l - paddingLeft - paddingRight;
        int layoutLeft = 0;
        int layoutTop = paddingTop;
        for(Line line : lines) {
            layoutLeft = paddingLeft;
            List<View> viewList = line.getViewList();
            if(viewList.size() <= 0){
                continue;
            }
            int lineHeight = line.getHeight();
            // 平分剩下的空间
            int avgSpace = (totalWidth - line.getWidth()) / viewList.size();
            avgSpace = avgSpace > 0 ? avgSpace : 0;
            //循环指定每一行中子View位置
            for(int i=0; i<viewList.size(); i++) {
                View child = viewList.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                // 重新测量
                child.measure(MeasureSpec.makeMeasureSpec(childWidth + avgSpace, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                childWidth = child.getMeasuredWidth();
                int left = layoutLeft;
                int top = layoutTop + (lineHeight - childHeight) / 2;
                int right = layoutLeft + childWidth;
                int bottom = top + childHeight;
                child.layout(left, top, right, bottom);
                layoutLeft = layoutLeft + childWidth + horizontalSpace;
            }
            layoutTop = layoutTop + lineHeight + verticalSpace;
        }
    }

    /**
     * 子View水平间距等量扩大，使子View在水平方向铺满
     * @param l FlowLayout相对于父控件的布局位置
     * @param t FlowLayout相对于父控件的布局位置
     * @param r FlowLayout相对于父控件的布局位置
     * @param b FlowLayout相对于父控件的布局位置
     */
    private void layoutAdjustSpace(int l, int t, int r, int b){
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int totalWidth = r - l - paddingLeft - paddingRight;
        int layoutLeft = 0;
        int layoutTop = paddingTop;
        for(Line line : lines){
            layoutLeft = paddingLeft;
            List<View> viewList = line.getViewList();
            if(viewList.size() <= 0){
                continue;
            }
            int lineHeight = line.getHeight();
            // 平分剩下的空间
            int avgSpace = (totalWidth - line.getWidth()) / (viewList.size() - 1);
            avgSpace = avgSpace > 0 ? avgSpace : 0;
            //横向多余空间平均分配到控件之间横向间距上
            int adjustSpace = horizontalSpace + avgSpace;
            //循环指定每一行中子View位置
            for(int i=0; i<viewList.size(); i++){
                View child = viewList.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int left = layoutLeft;
                //空间在每一行中垂直居中
                int top = layoutTop + (lineHeight - childHeight) / 2;
                int right = left + childWidth;
                int bottom = top + childHeight;
                child.layout(left, top, right, bottom);
                layoutLeft = layoutLeft + childWidth + adjustSpace;
            }
            layoutTop = layoutTop + lineHeight + verticalSpace;
        }
    }

    private class Line{
        List<View> viewList;
        int width;
        int height;

        private Line(){
            viewList = new ArrayList<>();
        }

        public List<View> getViewList() {
            return viewList;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void addView(View view){
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            if(viewList.size() == 0){
                width = viewWidth;
            }else {
                width = width + horizontalSpace + viewWidth;
            }
            height = height > viewHeight ? height : viewHeight;
            viewList.add(view);
        }

        public boolean canAddView(View view){
            if(viewList.size() == 0){
                return true;
            }
            int viewWidth = view.getMeasuredWidth();
            if(horizontalSpace + viewWidth > maxWidth - width){
                return false;
            }
            return true;
        }

        public void clear(){
            if(viewList != null){
                viewList.clear();
            }
            width = 0;
            height = 0;
        }
    }
}
