package sssta.mediarecordertry;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//定义了一些必要的控件和参数
	private Button bn_re;
	private Button bn_play;
	private EditText edit_name;
	private TextView info;
	
	//定义了几个String，path为存储路径，name为文件名，需要输入
	private final String path_ex="/mnt/sdcard/MediaRecorderTry";
	private final String path_bh=".3gp";
	private String path;
	private String name;
	
	//定义录制和播放最主要的两个实例，MediaPlayer,MediaRecoder
	private MediaPlayer mediaPlayer;
	private MediaRecorder mediaRecorder;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        createSDCardDir();
        
        //必要的初始化
        bn_play=(Button)findViewById(R.id.bn_play);
        bn_re=(Button)findViewById(R.id.bn_re);
        edit_name=(EditText)findViewById(R.id.edit_name);
        info=(TextView)findViewById(R.id.info);
        
        //预先设置三个控件的text，也作为控制的状态变量
        info.setText("猛敲上面录制声音");
        bn_re.setText("开始录制");
        bn_play.setText("播放声音");
        
        //设置录制按键的点击事件
        bn_re.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//在bn_re显示“开始录制”时触发录制事件
				if(bn_re.getText().toString().equals("开始录制"))
				{
					//防止EditText为空导致录入空文件名
					if(edit_name.getText().toString()!=null&&edit_name.getText().toString().length()!=0)
					{
						//定义个临时变量来存储EditText中的name
						String name_temp=edit_name.getText().toString();
						//设置TextView显示现在的状态
						info.setText("录制中。。。文件名为:"+name_temp);
						//将临时name赋值给name
						name=name_temp;
						path=path_ex+name+path_bh;
						//如果已经存在这个文件了，则提示直接播放
				    	if(fileIsExists(path))
				    	{
				    		info.setText("此文件已经存在，现在可以直接播放，但是就是不能录音");
				    		edit_name.setText("");
				    	}
				    	else
				    	{
				    		//看此方法定义，开始录制声音
							Record();
							//bn_re改变状态
							bn_re.setText("停止录制");
							//设置播放声音按键不可点击，防止出错
							bn_play.setClickable(false);
				    	}
						
					}
					else
					{
						//如果EditText是空的，显示提醒--！
						info.setText("你是不是傻啊？你是傻吧！没有文件名啊！");
					}
				}
				//在bn_re显示“停止录制”时触发停止录制事件
				else if(bn_re.getText().toString().equals("停止录制"))
				{
					//看此方法定义，停止录制声音
					StopRecord();
					//TextView显示现在的状态
					info.setText("录制完毕，文件名："+name);
					//bn_re改变状态
					bn_re.setText("开始录制");
					//设置播放按键可以点击
					bn_play.setClickable(true);
				}
			}
		});
        //设置播放按键的点击事件
        bn_play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//如果路径不为空，也就是已经有录制的声音了，就直接播放
				if(path!=null&&path.length()!=0)
				{
					//如果bn_play显示为播放声音
					if(bn_play.getText().toString().equals("播放声音"))
					{
						//播放声音，查看此方法定义
						Play();
						//一样的提示和更改按钮状态
						info.setText("别理我，播放声音ing");
						bn_play.setText("停止播放");
						bn_re.setClickable(false);
					}
					else if(bn_play.getText().toString().equals("停止播放"))
					{
						//停止播放声音，查看此方法定义
						StopPlay();
						//一样的提示和更改按钮状态
						info.setText("现在已经有声音，播放请敲下面，重新录制敲上面");
						bn_play.setText("播放声音");
						bn_re.setClickable(true);
					}
				}
				//如果路径为空，也就是没有录制的声音了，则提示
				else
				{
					//设置提醒
					info.setText("你是不是傻啊？你是傻吧！还没有录制声音啊！");
				}
			}
		});  
    }

    //录制声音的方法
    private void Record()
    {
    	//建立实例
    	mediaRecorder=new MediaRecorder();
    	//设置声音获取的方式，此处为麦克风
    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	//设置声音的封装格式
    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	//设置声音的编码格式
    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	//为path赋值
    	
    	//设置声音的保存路径
    	mediaRecorder.setOutputFile(path);
    	//try-catch语句是为了捕捉异常
    	try {
    		//准备和播放，拟写者两句，系统会自动提示捕捉异常
    		mediaRecorder.prepare();
    		mediaRecorder.start();
    		//下面是两个异常，自动添加
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //停止录制方法
    private void StopRecord()
    {
    	//首先是要stop，其次是要reset，此时MediaRecoder回复到初始化状态
    	mediaRecorder.stop();
    	mediaRecorder.reset();
    }
    
    protected void OnStop()
    {
    	//在程序退出时释放MediaRecoder和MediaPlayer两个资源
    	mediaRecorder.release();
    	mediaPlayer.release();
    	super.onStop();
    }
    //播放声音方法
    private void Play()
    {
    	mediaPlayer=new MediaPlayer();
    	//try-catch语句是为了捕捉异常
    	try {
    		//首先设置播放路径，之后准备并且开始播放
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();	
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    }
    //停止播放声音方法
    private void StopPlay()
    {
    	//首先是要stop，其次是要reset，此时MediaRecoder回复到初始化状态
    	mediaPlayer.stop();
    	mediaPlayer.reset();
    }
    //判断是否存在此文件
    public boolean fileIsExists(String path){
        try{
               	File f=new File(path);
                if(!f.exists())
                {
                	return false;
                }
                
        }catch (Exception e) {
                // TODO: handle exception
                return false;
        }
        return true;
    }
    //如果程序文件夹不存在，创建一个新的文件夹
    private void createSDCardDir(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
               // 创建一个文件夹对象，赋值为外部存储器的目录
                File sdcardDir =Environment.getExternalStorageDirectory();
              //得到一个路径，内容是sdcard的文件夹路径和名字
                String path=sdcardDir.getPath()+"/MediaRecorderTry";
                File path1 = new File(path);
               if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
               // setTitle("paht ok,path:"+path);
              }
               }
        else{
        	//setTitle("false");
        	return;
       }
       }

}
