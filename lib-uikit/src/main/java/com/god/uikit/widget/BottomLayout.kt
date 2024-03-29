package com.god.uikit.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import androidx.databinding.ObservableMap
import com.god.uikit.R
import com.god.uikit.databinding.LayoutBottomBinding
import com.god.uikit.utils.dip2Px
import com.god.uikit.utils.px2dp
import java.lang.IndexOutOfBoundsException

class BottomLayout : FrameLayout {
    lateinit var dataBinding : LayoutBottomBinding;

    lateinit var imageMap : ObservableMap<String,Drawable>;
    lateinit var itemNumbers : ObservableField<Int>;

    private var iconSel1: Drawable? = null
    private var iconSel2: Drawable? = null
    private var iconSel3: Drawable? = null
    private var iconSel4: Drawable? = null
    private var iconSel5: Drawable? = null

    private var iconNor1: Drawable? = null
    private var iconNor2: Drawable? = null
    private var iconNor3: Drawable? = null
    private var iconNor4: Drawable? = null
    private var iconNor5: Drawable? = null

    private var text1: String? = null
    private var text2: String? = null
    private var text3: String? = null
    private var text4: String? = null
    private var text5: String? = null

    private var selColor = 0

    private var norColor = 0

    private var lineColor = 0;

    //选中的导航栏，默认从1开始
    private var index: ObservableField<Int>? = null

    //底部文字
    private var textMap: ObservableArrayMap<String, String>? = null

    private var onBottomClickListener: OnBottomClickListener? = null

    private var haveLine = ObservableField<Boolean>(false);

    private var bottomTextSize : Float = 14.dip2Px().toFloat();

    constructor(context: Context) : super(context) {}
    constructor(
        context: Context?,
        attributeSet: AttributeSet?
    ) : super(context!!, attributeSet) {
        var ta = context.obtainStyledAttributes(attributeSet, R.styleable.BottomLayout);

        haveLine.set(ta.getBoolean(R.styleable.BottomLayout_lyn_haveLine,false));

        textMap = ObservableArrayMap<String, String>()
        text1 = ta.getString(R.styleable.BottomLayout_lyn_itemText_1)
        text2 = ta.getString(R.styleable.BottomLayout_lyn_itemText_2)
        text3 = ta.getString(R.styleable.BottomLayout_lyn_itemText_3)
        textMap!!["text1"] = text1
        textMap!!["text2"] = text2
        textMap!!["text3"] = text3

        //默认3个底部导航栏
        itemNumbers = ObservableField(ta.getInt(R.styleable.BottomLayout_lyn_itemNumbers,3));

        val tempBtp: Drawable = BitmapDrawable(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.icon_no_data_default
            )
        )
        imageMap = ObservableArrayMap();

        var bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_sel_1)
        if(bd == null){
            iconSel1 = tempBtp;
        }else{
            iconSel1 = bd;
        }
        bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_nor_1);
        if(bd == null){
            iconNor1 = tempBtp;
        }else{
            iconNor1 = bd;
        }

        bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_sel_2);
        if(bd == null){
            iconSel2 = tempBtp;
        }else{
            iconSel2 = bd;
        }

        bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_nor_2);
        if(bd == null){
            iconNor2 = tempBtp;
        }else{
            iconNor2 = bd;
        }

        bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_sel_3);
        if(bd == null){
            iconSel3 = tempBtp;
        }else{
            iconSel3 = bd;
        }

        bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_nor_3);
        if(bd == null){
            iconNor3 = tempBtp;
        }else{
            iconNor3 = bd;
        }
        imageMap["icon1sel"] = iconSel1;
        imageMap["icon2sel"] = iconSel2;
        imageMap["icon3sel"] = iconSel3;
        imageMap["icon1nor"] = iconNor1;
        imageMap["icon2nor"] = iconNor2;
        imageMap["icon3nor"] = iconNor3;

        if(itemNumbers.get()!! > 3) {
            bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_sel_4);
            if(bd == null){
                iconSel4 =  tempBtp;
            }else{
                iconSel4 = bd;
            }
            imageMap["icon4sel"] = iconSel4;
            bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_nor_4);
            if(bd == null){
                iconNor4 = tempBtp;
            }else{
                iconNor4 = bd;
            }
            imageMap["icon4nor"] = iconNor4;

            text4 = ta.getString(R.styleable.BottomLayout_lyn_itemText_4)
            textMap!!["text4"] = text4
        }

        if(itemNumbers.get()!! > 4){
            bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_sel_5);
            if(bd == null){
                iconSel5 = tempBtp;
            }else{
                iconSel5 = bd;
            }
            imageMap["icon5sel"] = iconSel5

            bd = ta.getDrawable(R.styleable.BottomLayout_lyn_iconSrc_nor_5);
            if(bd == null){
                iconNor5 = tempBtp;
            }else{
                iconNor5 = bd;
            }
            imageMap["icon5nor"] = iconNor5

            text5 = ta.getString(R.styleable.BottomLayout_lyn_itemText_5)
            textMap!!["text5"] = text5
        }

        selColor = ta.getColor(
            R.styleable.BottomLayout_lyn_selTextColor,
            resources.getColor(R.color.colorApp)
        )
        norColor = ta.getColor(
            R.styleable.BottomLayout_lyn_norTextColor,
            resources.getColor(R.color.colorHint)
        )

        lineColor = ta.getColor(R.styleable.BottomLayout_lyn_lineColor,
            resources.getColor(R.color.colorLineLight));

        bottomTextSize = ta.getDimension(R.styleable.BottomLayout_lyn_bottomTextSize,15.dip2Px().toFloat());

        ta.recycle();
        ta = null;
        initAttributeSet()
    }

    fun initAttributeSet(){
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_bottom,
            this,
            true
        )
        dataBinding.bottomLayout = this
        dataBinding.haveLine = haveLine;
        if(haveLine.get()!!){
            dataBinding.lineView.setBackgroundColor(lineColor);
        }
        dataBinding.imageMap = imageMap
        dataBinding.itemNumbers = itemNumbers

        dataBinding.norTextColor = norColor
        dataBinding.selTextColor = selColor

        dataBinding.textMap = textMap

        index = ObservableField(1)
        dataBinding.itemIndex = index

        dataBinding.bottomText1.setTextSize(TypedValue.COMPLEX_UNIT_PX,bottomTextSize);
        dataBinding.bottomText2.setTextSize(TypedValue.COMPLEX_UNIT_PX,bottomTextSize);
        dataBinding.bottomText3.setTextSize(TypedValue.COMPLEX_UNIT_PX,bottomTextSize);
        dataBinding.bottomText4.setTextSize(TypedValue.COMPLEX_UNIT_PX,bottomTextSize);
    }

    /**
     * 底部点击事件处理
     *
     * create by Administrator at 2020/3/27 3:28
     * @author Administrator
     * @since 1.0.0
     * @param view 触发点击事件的View
     * @return
     * void
     */
    fun onBottomClick(view: View) {
        val id = view.id
        if (id == R.id.item_layout_1) {
            index!!.set(1)
        } else if (id == R.id.item_layout_2) {
            index!!.set(2)
        } else if (id == R.id.item_layout_3) {
            index!!.set(3)
        } else if (id == R.id.item_layout_4) {
            index!!.set(4)
        } else if (id == R.id.item_layout_5) {
            index!!.set(5)
        }
        if (onBottomClickListener != null) {
            index!!.get()?.let {
                onBottomClickListener!!.onBottomClick(it)
            }
        }
    }

    fun setOnBottomClickListener(onBottomClickListener: OnBottomClickListener) {
       this.onBottomClickListener = onBottomClickListener;
    }

    fun setSelect(index : Int){
        this.index!!.set(index);
    }

    fun resetBottom(textList : MutableList<String>,norImageList : MutableList<Int>,
                    selImageList : MutableList<Int>){
        if(norImageList.size != selImageList.size){
            throw IllegalAccessException("the norImageList number size must be the same of selImageList size");
            return;
        }
        if(textList.size != norImageList.size){
            throw IllegalAccessException("the textMap number size must be the same of norImageList size");
            return;
        }
        if(textList.size > itemNumbers.get()?:0){
            throw IndexOutOfBoundsException("the textMap size out of itemNumbers")
        }
        val textkeyList = arrayListOf("text1","text2","text3","text4","text5");
        val selKeyList = arrayListOf("icon1sel","icon2sel","icon3sel","icon4sel","icon5sel");
        val norKeyList = arrayListOf("icon1nor","icon2nor","icon3nor","icon4nor","icon5nor");
        var i = 0;
        textMap?.clear();
        imageMap.clear();
        textList.forEach {
            textMap?.put(textkeyList.get(i), it);
            i++;
        }
        i = 0;
        selImageList.forEach {
            imageMap.put(selKeyList.get(i),BitmapDrawable(BitmapFactory.decodeResource(resources, it)));
            imageMap.put(norKeyList.get(i),BitmapDrawable(BitmapFactory.decodeResource(resources, norImageList.get(i))));
            i++;
        }
    }

    fun setBottomNumber(number:Int){
        itemNumbers.set(number);
    }

    fun setText(index: Int,text:String){
        textMap?.setValueAt(index,text);
    }

    fun setImage(index : Int,norImageResourceId : Int,selImageResourceId : Int){
        if(index < itemNumbers.get()?:0){
            when(index){
                0->{
                    imageMap.set("icon1sel",BitmapDrawable(BitmapFactory.decodeResource(resources, selImageResourceId)))
                    imageMap.set("icon1nor",BitmapDrawable(BitmapFactory.decodeResource(resources, norImageResourceId)))
                }
                1->{
                    imageMap.set("icon2sel",BitmapDrawable(BitmapFactory.decodeResource(resources, selImageResourceId)))
                    imageMap.set("icon2nor",BitmapDrawable(BitmapFactory.decodeResource(resources, norImageResourceId)))
                }
                2->{
                    imageMap.set("icon3sel",BitmapDrawable(BitmapFactory.decodeResource(resources, selImageResourceId)))
                    imageMap.set("icon3nor",BitmapDrawable(BitmapFactory.decodeResource(resources, norImageResourceId)))
                }
                3->{
                    imageMap.set("icon4sel",BitmapDrawable(BitmapFactory.decodeResource(resources, selImageResourceId)))
                    imageMap.set("icon4nor",BitmapDrawable(BitmapFactory.decodeResource(resources, norImageResourceId)))
                }
                4->{
                    imageMap.set("icon5sel",BitmapDrawable(BitmapFactory.decodeResource(resources, selImageResourceId)))
                    imageMap.set("icon5nor",BitmapDrawable(BitmapFactory.decodeResource(resources, norImageResourceId)))
                }
            }
        }
    }


    fun twoAndThree(operation: (Int, Int) -> Int){
        //调用函数类型的参数
        val result = operation(2, 3)
        println("The result is $result")
    }

    fun main(arg: Array<String>) {
        twoAndThree{ a, b -> a + b}
        twoAndThree{ a, b -> a * b}
    }


    fun getOnBottomClickListener(): OnBottomClickListener? {
        return onBottomClickListener
    }

    interface OnBottomClickListener {
        /**
         * 底部导航栏点击事件监听
         * @author hux
         * @createTime 2019/5/5 16:15
         * @since 1.0.0
         * @see BottomLayout
         *
         * @param position
         * 选中的底部导航栏下标，从1开始
         * @return
         * void
         */
        fun onBottomClick(position: Int)
    }
}
