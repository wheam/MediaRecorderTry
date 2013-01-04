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

	//������һЩ��Ҫ�Ŀؼ��Ͳ���
	private Button bn_re;
	private Button bn_play;
	private EditText edit_name;
	private TextView info;
	
	//�����˼���String��pathΪ�洢·����nameΪ�ļ�������Ҫ����
	private final String path_ex="/mnt/sdcard/MediaRecorderTry";
	private final String path_bh=".3gp";
	private String path;
	private String name;
	
	//����¼�ƺͲ�������Ҫ������ʵ����MediaPlayer,MediaRecoder
	private MediaPlayer mediaPlayer;
	private MediaRecorder mediaRecorder;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        createSDCardDir();
        
        //��Ҫ�ĳ�ʼ��
        bn_play=(Button)findViewById(R.id.bn_play);
        bn_re=(Button)findViewById(R.id.bn_re);
        edit_name=(EditText)findViewById(R.id.edit_name);
        info=(TextView)findViewById(R.id.info);
        
        //Ԥ�����������ؼ���text��Ҳ��Ϊ���Ƶ�״̬����
        info.setText("��������¼������");
        bn_re.setText("��ʼ¼��");
        bn_play.setText("��������");
        
        //����¼�ư����ĵ���¼�
        bn_re.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//��bn_re��ʾ����ʼ¼�ơ�ʱ����¼���¼�
				if(bn_re.getText().toString().equals("��ʼ¼��"))
				{
					//��ֹEditTextΪ�յ���¼����ļ���
					if(edit_name.getText().toString()!=null&&edit_name.getText().toString().length()!=0)
					{
						//�������ʱ�������洢EditText�е�name
						String name_temp=edit_name.getText().toString();
						//����TextView��ʾ���ڵ�״̬
						info.setText("¼���С������ļ���Ϊ:"+name_temp);
						//����ʱname��ֵ��name
						name=name_temp;
						path=path_ex+name+path_bh;
						//����Ѿ���������ļ��ˣ�����ʾֱ�Ӳ���
				    	if(fileIsExists(path))
				    	{
				    		info.setText("���ļ��Ѿ����ڣ����ڿ���ֱ�Ӳ��ţ����Ǿ��ǲ���¼��");
				    		edit_name.setText("");
				    	}
				    	else
				    	{
				    		//���˷������壬��ʼ¼������
							Record();
							//bn_re�ı�״̬
							bn_re.setText("ֹͣ¼��");
							//���ò��������������ɵ������ֹ����
							bn_play.setClickable(false);
				    	}
						
					}
					else
					{
						//���EditText�ǿյģ���ʾ����--��
						info.setText("���ǲ���ɵ��������ɵ�ɣ�û���ļ�������");
					}
				}
				//��bn_re��ʾ��ֹͣ¼�ơ�ʱ����ֹͣ¼���¼�
				else if(bn_re.getText().toString().equals("ֹͣ¼��"))
				{
					//���˷������壬ֹͣ¼������
					StopRecord();
					//TextView��ʾ���ڵ�״̬
					info.setText("¼����ϣ��ļ�����"+name);
					//bn_re�ı�״̬
					bn_re.setText("��ʼ¼��");
					//���ò��Ű������Ե��
					bn_play.setClickable(true);
				}
			}
		});
        //���ò��Ű����ĵ���¼�
        bn_play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//���·����Ϊ�գ�Ҳ�����Ѿ���¼�Ƶ������ˣ���ֱ�Ӳ���
				if(path!=null&&path.length()!=0)
				{
					//���bn_play��ʾΪ��������
					if(bn_play.getText().toString().equals("��������"))
					{
						//�����������鿴�˷�������
						Play();
						//һ������ʾ�͸��İ�ť״̬
						info.setText("�����ң���������ing");
						bn_play.setText("ֹͣ����");
						bn_re.setClickable(false);
					}
					else if(bn_play.getText().toString().equals("ֹͣ����"))
					{
						//ֹͣ�����������鿴�˷�������
						StopPlay();
						//һ������ʾ�͸��İ�ť״̬
						info.setText("�����Ѿ��������������������棬����¼��������");
						bn_play.setText("��������");
						bn_re.setClickable(true);
					}
				}
				//���·��Ϊ�գ�Ҳ����û��¼�Ƶ������ˣ�����ʾ
				else
				{
					//��������
					info.setText("���ǲ���ɵ��������ɵ�ɣ���û��¼����������");
				}
			}
		});  
    }

    //¼�������ķ���
    private void Record()
    {
    	//����ʵ��
    	mediaRecorder=new MediaRecorder();
    	//����������ȡ�ķ�ʽ���˴�Ϊ��˷�
    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	//���������ķ�װ��ʽ
    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	//���������ı����ʽ
    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	//Ϊpath��ֵ
    	
    	//���������ı���·��
    	mediaRecorder.setOutputFile(path);
    	//try-catch�����Ϊ�˲�׽�쳣
    	try {
    		//׼���Ͳ��ţ���д�����䣬ϵͳ���Զ���ʾ��׽�쳣
    		mediaRecorder.prepare();
    		mediaRecorder.start();
    		//�����������쳣���Զ����
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //ֹͣ¼�Ʒ���
    private void StopRecord()
    {
    	//������Ҫstop�������Ҫreset����ʱMediaRecoder�ظ�����ʼ��״̬
    	mediaRecorder.stop();
    	mediaRecorder.reset();
    }
    
    protected void OnStop()
    {
    	//�ڳ����˳�ʱ�ͷ�MediaRecoder��MediaPlayer������Դ
    	mediaRecorder.release();
    	mediaPlayer.release();
    	super.onStop();
    }
    //������������
    private void Play()
    {
    	mediaPlayer=new MediaPlayer();
    	//try-catch�����Ϊ�˲�׽�쳣
    	try {
    		//�������ò���·����֮��׼�����ҿ�ʼ����
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
    //ֹͣ������������
    private void StopPlay()
    {
    	//������Ҫstop�������Ҫreset����ʱMediaRecoder�ظ�����ʼ��״̬
    	mediaPlayer.stop();
    	mediaPlayer.reset();
    }
    //�ж��Ƿ���ڴ��ļ�
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
    //��������ļ��в����ڣ�����һ���µ��ļ���
    private void createSDCardDir(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
               // ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
                File sdcardDir =Environment.getExternalStorageDirectory();
              //�õ�һ��·����������sdcard���ļ���·��������
                String path=sdcardDir.getPath()+"/MediaRecorderTry";
                File path1 = new File(path);
               if (!path1.exists()) {
                //�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
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
