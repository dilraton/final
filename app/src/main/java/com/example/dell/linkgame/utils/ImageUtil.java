package com.example.dell.linkgame.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.dell.linkgame.R;
import com.example.dell.linkgame.view.LinkPieceImage;

/**
 * 图片资源工具类, 主要用于获取要显示的游戏图片
 */
public class ImageUtil {

    /**
     *  获取drawable中相应主题所有图片的ID
     */
    public static List<Integer> getImageValues(String theme) {
        try {
            Field[] drawableFields = R.drawable.class.getDeclaredFields(); // 得到R.drawable所有的属性, 即获取drawable目录下的所有图片
            List<Integer> resourceValues = new ArrayList<Integer>();
            for (Field field : drawableFields) {
                if (field.getName().startsWith(theme+"_")) {// 如果该Field的名称以p_开头
                    resourceValues.add(field.getInt(R.drawable.class));
                }
            }
            return resourceValues;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 随机从sourceValues的集合中获取size个图片ID, 返回结果为图片ID的集合
     * @param sourceValues 从drawable中获取的相应的图片集合
     * @param size 需要随机获取的图片个数
     * @return size个图片ID的集合
     */
    public static List<Integer> getRandomValues(List<Integer> sourceValues, int size) {
        Random random = new Random(); // 创建一个随机数生成器
        List<Integer> result = new ArrayList<Integer>(); // 创建结果集合
        for (int i = 0; i < size; i++) {
            try {
                int index = random.nextInt(sourceValues.size());// 随机获取一个数字，大于等于0、小于sourceValues.size()的数值
                Integer image = sourceValues.get(index);    // 从图片ID集合中获取该图片对象
                result.add(image);    // 添加到结果集中
            } catch (IndexOutOfBoundsException e) {
                return result;
            }
        }
        return result;
    }

    /**
     * 真正获取要显示的图片ID集合
     * @param size 需要获取的图片ID的数量
     * @return 要显示的size个图片ID的集合
     */
    public static List<Integer> getPlayValues(int size,String theme) {
        if (size % 2 != 0) {// 如果该数除2有余数，将size加1
            size += 1;
        }
        List<Integer> imageValues = getImageValues(theme); // 获取相应主题的所有连连看图片ID值
        List<Integer> playImageValues = getRandomValues(imageValues, size / 2);  // 从相应主题所有的图片ID值中随机获取size一半数量,即size/2张图片的ID
        playImageValues.addAll(playImageValues);  // 将playImageValues集合的元素增加一倍（保证所有图片都有与之配对的图片），即size张图片
        Collections.shuffle(playImageValues);  // 将所有图片ID随机“洗牌”
        return playImageValues;
    }

    /**
     * 将图片ID集合转换LinkPieceImage对象集合，得到要显示的最终的LinkPieceImage集合
     * @param context
     * @param size
     * @return size个LinkPieceImage对象的集合
     */
    public static List<LinkPieceImage> getPlayImages(Context context, int size,String theme) {
        List<Integer> resourceValues = getPlayValues(size,theme);// 获取要显示的图片ID的集合
        List<LinkPieceImage> result = new ArrayList<LinkPieceImage>();
        for (Integer value : resourceValues) { // 遍历每个图片ID
           // Bitmap bm = BitmapFactory.decodeResource(context.getResources(), value);// 加载每个ID对应的图片
            Drawable d = context.getResources().getDrawable(value);
            Bitmap bm = Bitmap.createBitmap(GameConfig.PIECE_WIDTH,GameConfig.PIECE_WIDTH,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            d.setBounds(0, 0, GameConfig.PIECE_WIDTH, GameConfig.PIECE_WIDTH);
            d.draw(canvas);
            LinkPieceImage linkPieceImage = new LinkPieceImage(bm, value); // 封装图片ID与图片本身
            result.add(linkPieceImage); //将linkPieceImage加入结果集合中
        }
        return result;

    }

    /**
     *  获取选中标识的图片
     * @param context
     * @return 选中标识的图片
     */
    public static Bitmap getSelectImage(Context context) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.selected);
        return bm;
    }
}
