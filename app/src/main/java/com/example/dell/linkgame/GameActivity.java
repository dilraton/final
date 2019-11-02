package com.example.dell.linkgame;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dell.linkgame.board.GameService;
import com.example.dell.linkgame.impl.GameServiceImpl;
import com.example.dell.linkgame.utils.ExitApplication;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.utils.LinkPointsInfo;
import com.example.dell.linkgame.view.GameView;
import com.example.dell.linkgame.view.LinkPiece;


/**
 * 游戏Activity
 */
public class GameActivity extends AppCompatActivity {

    private GameConfig config; //游戏配置对象
    private GameService gameService; //游戏业务逻辑接口
    private ProgressBar progressBar;
    private GameView gameView; //游戏区域
    private ImageButton backButton;//返回上级按钮按钮
    private ImageButton pauseButton; //游戏暂停按钮
    private TextView scoreTextView;//记录分数的文本框
    private ImageButton replayButton; //重新开始按钮
    private ImageButton tipButton; //提示按钮
    private ImageButton menuButton; //返回菜单按钮
    private ImageView showImage;//遮挡控件
    private AlertDialog.Builder failureDialog;//失败后弹出的对话框
    private AlertDialog.Builder successDialog;//游戏胜利后的对话框
    private AlertDialog.Builder dieDialog;//出现死局时的对话框
    private AlertDialog.Builder cueDialog;//出现死局时的对话框
    private Timer timer = new Timer();//定时器
    private int gameTime;//记录游戏的剩余时间
    private int score;//记录分数
    private boolean isPlaying;//记录是否处于游戏状态
    private boolean isPause;//记录是否处于暂停状态
    private String theme;//记录游戏主题
    private int mode;//记录游戏模式
    private Vibrator vibrator;//振动处理类
    private LinkPiece selectedPiece = null;//记录已经选中的方块
    private boolean isMusic;
    private String TAG = "GameActivity";
    //Handler类，异步处理
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    progressBar.incrementProgressBy(-1);
                    gameTime--; // 游戏剩余时间减少
                    // 时间小于0, 游戏失败
                    if (gameTime < 0) {
                        stopTimer();// 停止计时
                        isPlaying = false; // 更改游戏的状态
                        failureDialog.show();// 失败后弹出对话框
                        return;
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ExitApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        theme = intent.getStringExtra("theme");
        isMusic = intent.getBooleanExtra("isMusic",false);
        mode = intent.getIntExtra("mode",0);
        init();// 初始化界面
        startGame(GameConfig.DEFAULT_TIME,true);
    }

    /**
     * 初始化游戏
     */
    private void init() {

        config = new GameConfig(8, 8, 1, 10, GameConfig.DEFAULT_TIME, this);

        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);// 获取进度条
        gameView = (GameView) this.findViewById(R.id.gameView);// 得到游戏区域对象
        backButton = (ImageButton) this.findViewById(R.id.imageButtonBack);// 获取返回上级按钮
        pauseButton = (ImageButton) this.findViewById(R.id.imageButtonPause);// 获取游戏暂停按钮
        scoreTextView = (TextView) this.findViewById(R.id.textViewScore2);// 获取显示分数的文本框
        replayButton = (ImageButton) this.findViewById(R.id.imageButtonReplay);// 获取开始按钮
        tipButton = (ImageButton) this.findViewById(R.id.imageButtonTip);// 获取提示按钮
        menuButton = (ImageButton) this.findViewById(R.id.imageButtonMenu);// 获取返回菜单按钮
        showImage = (ImageView)this.findViewById(R.id.gifImageView); // 获取遮挡控件

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);// 获取振动器
        gameService = new GameServiceImpl(this.config); // 初始化游戏业务逻辑接口
        gameView.setGameService(gameService); // 设置游戏逻辑的实现类

        //为返回上级按钮的单击事件绑定事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent config = new Intent(GameActivity.this,RoundActivity.class);
                config.putExtra("theme",theme);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

        //为游戏暂停按钮的单击事件绑定事件监听器
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {

                if (!isPause){
                    pause();
                }else{
                    resume();
                }

            }
        });

        //为重新开始按钮的单击事件绑定事件监听器
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                startGame(GameConfig.DEFAULT_TIME,true);
            }
        });

        //为提示开始按钮的单击事件绑定事件监听器
        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                gameCue();
            }
        });

        //为返回菜单按钮的单击事件绑定事件监听器
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Log.i(TAG, "menu!");
                Intent config = new Intent(GameActivity.this,MainActivity.class);
                config.putExtra("isNew",false);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

        // 为游戏区域的触碰事件绑定监听器
        this.gameView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    gameViewTouchDown(e);
                }
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    gameViewTouchUp(e);
                }
                return true;
            }
        });

        // 初始化游戏失败的对话框
        failureDialog = createDialog("游戏结束", "游戏失败！ 重新开始", R.drawable.failure).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startGame(GameConfig.DEFAULT_TIME,true);
                    }
        });

        // 初始化出现死局时的对话框
        dieDialog = createDialog("死局提示", "出现死局，已自动消除一对图片！", R.drawable.success).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // 初始化不可用提示功能的对话框
        cueDialog = createDialog("提示", "您当前的分数不支持提示功能！", R.drawable.failure).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    @Override
    protected void onPause() {// 结束游戏
        if(!isPause){
            stopTimer();
        }
        super.onPause();
    }

    private void pause(){// 暂停游戏
        Log.i(TAG, "onPause!");
        stopTimer();
        pauseButton.setImageResource(R.mipmap.play);
        gameView.setVisibility(View.INVISIBLE);
        showImage.setVisibility(View.VISIBLE);
        isPause = true;
    }

    @Override
    protected void onResume() {
        // 如果处于游戏状态中
        if (isPlaying) {
            // 以剩余时间重新开始游戏
            startGame(gameTime,false);
        }
        super.onResume();
    }

    private void resume(){ //游戏继续
        if (isPlaying) {
            // 以剩余时间重新开始游戏
            startGame(gameTime,false);
            pauseButton.setImageResource(R.mipmap.pause);
            gameView.setVisibility(View.VISIBLE);
            showImage.setVisibility(View.INVISIBLE);
            isPause = false;
        }
    }

    /**
     * 游戏提示的处理方法
     */
    private void gameCue(){
        if(Integer.parseInt(scoreTextView.getText().toString())>0){
            LinkPiece[][] pieces = gameService.getLinkPieces(); // 获取当前的LinkPiece[][]数组
            List<LinkPiece> linkList = this.gameService.autoFindCueLink();
            if(!linkList.isEmpty()){
                LinkPiece piece1 = linkList.get(0);
                this.gameView.setSelectedLinkPiece(piece1); // 将方块piece1设为gameView中已选中的方块
                this.selectedPiece = piece1; // 并将当前方块设为activity中已选中的方块
                this.gameView.postInvalidate(); //刷新游戏区域
                LinkPiece piece2 = linkList.get(1);
                handleSuccessLink(this.gameService.link(piece1,piece2), this.selectedPiece, piece2, pieces);// 处理成功连接
                this.score -= 10;
                scoreTextView.setText(String.valueOf(score));
                gameSuccessHandle();
                this.gameView.postInvalidate(); //刷新游戏区域
                if(this.gameService.IsDie()){
                    List<LinkPiece> dLinkList = this.gameService.autoLink();
                    if(!dLinkList.isEmpty()){
                        LinkPiece dPiece1 = dLinkList.get(0);
                        LinkPiece dPiece2 = dLinkList.get(1);
                        pieces[dPiece1.getIndexX()][dPiece1.getIndexY()] = null;
                        pieces[dPiece2.getIndexX()][dPiece2.getIndexY()] = null;
                        this.gameView.postInvalidate();
                        this.dieDialog.show();//死局提示对话框显示

                    }
                }
            }
        }else{
            this.cueDialog.show();
        }
    }

    /**
     * 触碰游戏区域的处理方法
     */
    private void gameViewTouchDown(MotionEvent event) {

        LinkPiece[][] pieces = gameService.getLinkPieces(); // 获取当前的LinkPiece[][]数组
        float touchX = event.getX();// 获取用户点击的X坐标
        float touchY = event.getY();// 获取用户点击的Y坐标
        LinkPiece currentPiece = gameService.findLinkPiece(touchX, touchY);// 根据用户点击的坐标得到对应的LinkPiece对象

        if (currentPiece == null) // 如果没有选中任何LinkPiece对象，即鼠标点击的地方没有图片,返回
            return;

        if (this.selectedPiece == null) { // 如果之前没有选中任何一个方块
            this.gameView.setSelectedLinkPiece(currentPiece); // 将当前方块设为gameView中已选中的方块
            this.selectedPiece = currentPiece; // 并将当前方块设为activity中已选中的方块
            this.gameView.postInvalidate(); //刷新游戏区域，并返回
            return;
        }

        if (this.selectedPiece != null) {// 如果之前已经选中了一个方块
            LinkPointsInfo linkInfo = this.gameService.link(this.selectedPiece, currentPiece);//判断currentLinkPiece和preLinkPiece是否可以进行连接

            if (linkInfo == null) { // linkInfo为null，即两个LinkPiece不可连接
                this.gameView.setSelectedLinkPiece(currentPiece); // 将当前方块设为gameView中已选中的方块
                this.selectedPiece = currentPiece; // 并将当前方块设为activity中已选中的方块
                this.gameView.postInvalidate(); //刷新游戏区域
            } else {
                handleSuccessLink(linkInfo, this.selectedPiece, currentPiece, pieces);// 否则处理成功连接
                this.score += 10;
                scoreTextView.setText(String.valueOf(score));
                gameSuccessHandle();
            }
        }
    }

    /**
     * 触碰游戏区域的处理方法
     *
     * @param e
     */
    private void gameViewTouchUp(MotionEvent e) {
        this.gameView.postInvalidate();
        LinkPiece[][] pieces = gameService.getLinkPieces(); // 获取当前的LinkPiece[][]数组
        if(this.gameService.IsDie()){
            List<LinkPiece> linkList = this.gameService.autoLink();
            if(!linkList.isEmpty()){
                LinkPiece piece1 = linkList.get(0);
                LinkPiece piece2 = linkList.get(1);
                pieces[piece1.getIndexX()][piece1.getIndexY()] = null;
                pieces[piece2.getIndexX()][piece2.getIndexY()] = null;
                this.gameView.postInvalidate();
                this.dieDialog.show();//死局提示对话框显示

            }
        }
    }

    /**
     * 以gameTime作为剩余时间开始或恢复游戏
     *
     * @param gameTime
     *            剩余时间
     */
    private void startGame(int gameTime,boolean isNewGame) {
        // 如果之前的timer还未取消，取消timer
        if (this.timer != null) {
            stopTimer();
        }
        // 重新设置游戏时间
        this.gameTime = gameTime;
        this.progressBar.setProgress(gameTime);

        if(isNewGame){
            this.score = 0; // 重新设置游戏得分
            this.scoreTextView.setText("0");
        }else{
            this.scoreTextView.setText(String.valueOf(score));
        }

        // 如果游戏剩余时间与总游戏时间相等，即为重新开始新游戏
        if (gameTime == GameConfig.DEFAULT_TIME) {
            gameView.startGame(theme,mode);// 开始新的游戏游戏
        }
        isPlaying = true;
        isPause = false;
        this.timer = new Timer();
        // 启动计时器 ， 每隔1秒发送一次消息
        this.timer.schedule(new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 1000);

        this.selectedPiece = null; // 将选中方块设为null
        this.gameView.setSelectedLinkPiece(null);
        LinkPiece[][] pieces = gameService.getLinkPieces(); // 获取当前的LinkPiece[][]数组
        if(gameService.IsDie()){
            Log.i(TAG, "die" );
            List<LinkPiece> linkList = gameService.autoLink();
            if( ! linkList.isEmpty()){
                LinkPiece piece1 = linkList.get(0);
                LinkPiece piece2 = linkList.get(1);
                if(piece1 != null && piece2 != null){
                    pieces[piece1.getIndexX()][piece1.getIndexY()] = null;
                    pieces[piece2.getIndexX()][piece2.getIndexY()] = null;
                    gameView.invalidate();
                    dieDialog.show();//死局提示对话框显示
                }
            }
        }
    }

    /**
     * 成功连接后处理
     *  @param linkInfo
     *            连接信息
     * @param prePiece
     *            前一个选中方块
     * @param currentPiece
     *            当前选择方块
     * @param pieces
     */
    private void handleSuccessLink(LinkPointsInfo linkInfo, LinkPiece prePiece, LinkPiece currentPiece, LinkPiece[][] pieces) {
        this.gameView.setLinkPointsInfo(linkInfo);// 它们可以相连, 让GamePanel处理LinkInfo
        this.gameView.setSelectedLinkPiece(null);// 将gameView中的选中方块设为null
        this.gameView.postInvalidate();
        pieces[prePiece.getIndexX()][prePiece.getIndexY()] = null;// 将两个LinkPiece对象从数组中删除
        pieces[currentPiece.getIndexX()][currentPiece.getIndexY()] = null;
        this.selectedPiece = null;// 将选中的方块设置null
        this.vibrator.vibrate(100);// 手机振动(100毫秒)
    }

    /**
     * 游戏成功的判断及处理
     */
    private void gameSuccessHandle(){
        if (!this.gameService.hasLinkPieces()) { // 判断是否还有剩下的方块, 如果没有, 游戏胜利
            // 初始化游戏胜利的对话框
            successDialog = createDialog("游戏结束", "游戏胜利，本局得分：" + scoreTextView.getText() + "分！ 再来一局", R.drawable.success).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startGame(GameConfig.DEFAULT_TIME,true);
                }
            });
            this.successDialog.show();// 游戏胜利
            stopTimer();// 停止定时器
            isPlaying = false;// 更改游戏状态
        }
    }

    /**
     * 创建对话框的工具方法
     * @param title 标题
     * @param message 内容
     * @param imageResource 图片
     * @return
     */
    private AlertDialog.Builder createDialog(String title, String message, int imageResource) {
        return new AlertDialog.Builder(this).setTitle(title).setMessage(message).setIcon(imageResource);
    }

    /**
     * 停止计时
     */
    private void stopTimer() {
        this.timer.cancel();
        this.timer = null;
    }
}
