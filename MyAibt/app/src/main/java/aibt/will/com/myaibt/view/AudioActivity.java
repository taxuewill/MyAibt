package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import aibt.will.com.myaibt.R;

import static aibt.will.com.myaibt.R.id.btnPlay;

/**
 * @author 王晶
 * @date 17-6-26-下午2:17
 * @desc
 * http://blog.csdn.net/chenjie19891104/article/details/6333553
 */
public class AudioActivity extends Activity implements View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    private static final String TAG = "AudioActivity";


    //判断播放 状态 true:暂停  false:播放
    private boolean isPause = false;
    private MediaPlayer mp;
//    private MediaRecorder recorder;
    String musicPath = "/storage/sdcard1/张小七-单眼皮女生 (Live).mp3";

    //语音文件保存路径
    private String FileName = null;


    private File audioFile;
    private boolean isRecording=true, isPlaying=false; //标记
    private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private RecordTask recorder;
    private PlayTask player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        mp = new MediaPlayer();

        mp.setOnPreparedListener(this);
        mp.setOnErrorListener(this);

        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        FileName += "/audiorecordtest.3gp";
        FileName += "/audiorecord.pcm";
        audioFile = new File(FileName);

    }





    /**
     * 按钮 单击 事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG,"onClick ,v .getId is " + v.getId());
        switch (v.getId()) {
            case btnPlay:
                play();
                break;
            case R.id.btnPause:
                pause();

                break;
            case R.id.startRecord:
                startRecord();
                break;
            case R.id.stopRecord:
                stopRecord();
                break;
            case R.id.startPlay:
                startPlayRecord();
                break;
            case R.id.stopPlay:
                stopPlayRecord();
                break;

        }
    }


    /**
     * 暂停
     */
    private void pause() {
        if (mp.isPlaying()) {
            mp.pause();
            isPause = true;
        }

    }

    /**
     * 播放
     */
    private void play() {
        Log.i(TAG,"play ,isPause:"+isPause);
        if (isPause) {//如果是暂停，则直接播放
            mp.start();
            isPause = false;

        } else {//如果不是暂停，则要选回到空闲状态，再进行播放
            start();
        }

    }

    /**
     * 需要从头开始播放
     */
    private void start() {
            Log.i(TAG,"start,"+Environment.getExternalStorageDirectory().toString());
            File f = new File(musicPath);
            if(f.exists()){
                Log.i(TAG,"music file exist");
            }

            File sdcard = new File(Environment.getExternalStorageDirectory().toString()+"/Tez Cadey - Seve.mp3");
            if(sdcard.exists()){
                Log.i(TAG,"sdcard music exist");
            }
            //重置

            mp.reset();
            try {
                mp.setDataSource(Environment.getExternalStorageDirectory().toString()+"/Tez Cadey - Seve.mp3");
                mp.prepareAsync();//异步准备完毕，则开始播放
                isPause = false;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG,"start error "+e.toString());
            }



    }


    /**
     * prepareAsync() 方法完成后，回调这个方法
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    /**
     * 出现 错误时
     *
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return true;
    }

    /**
     * 音乐播放 完毕
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * activity 销毁时
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
        }
    }


    private void startRecord(){
        Log.i(TAG,"startRecord");
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(FileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        try {
//            recorder.prepare();
//        } catch (IOException e) {
//            Log.e(TAG, "prepare() failed");
//        }
//        recorder.start();
        recorder = new RecordTask();
        recorder.execute();



    }


    private void stopRecord(){
        Log.i(TAG,"stopRecord");
//        recorder.stop();
//        recorder.release();
//        recorder = null;


        this.isRecording = false;
    }



    private void startPlayRecord(){
        Log.i(TAG,"startPlayRecord,"+FileName);
//        File f = new File(FileName);
//        if(f.exists()){
//            Log.i(TAG,"music file exist");
//        }
//
//        //重置
//
//        mp.reset();
//        try {
//            mp.setDataSource(FileName);
//            mp.prepareAsync();//异步准备完毕，则开始播放
//            isPause = false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i(TAG,"start error "+e.toString());
//        }

        player = new PlayTask();
        player.execute();

    }

    private void stopPlayRecord(){
        isPlaying = false;
    }


    class RecordTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... arg0){
            isRecording = true;
            try{
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));
                int bufferSize = AudioRecord.getMinBufferSize(frequence,channelConfig,audioEncoding);
                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding,bufferSize);
                short[] buffer = new short[bufferSize/4];
                record.startRecording();
                int r = 0;
                while(isRecording){
                    int bufferReadResult = record.read(buffer,0,buffer.length);
                    for(int i = 0; i < bufferReadResult; i++){
                        dos.writeShort(buffer[i]);
                    }
                }

                record.stop();
                Log.v(TAG, "stop recorder ::"+audioFile.length());
                dos.close();

            }catch (Exception e){
                Log.i(TAG,"recordtask error "+e.toString());
            }

            return null;
        }
    }


    class PlayTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... arg0) {
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequence, channelConfig, audioEncoding);
            short[] buffer = new short[bufferSize/4];
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
                //实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, frequence, channelConfig, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                track.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while(isPlaying && dis.available()>0){
                    int i = 0;
                    while(dis.available()>0 && i<buffer.length){
                        buffer[i] = dis.readShort();
                        i++;
                    }
                    //然后将数据写入到AudioTrack中
                    track.write(buffer, 0, buffer.length);

                }

                //播放结束
                track.stop();
                dis.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }
    }

}
