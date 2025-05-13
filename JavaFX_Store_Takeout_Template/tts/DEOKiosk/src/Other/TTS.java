package Other;
import java.io.File;
import javax.sound.sampled.*;

public class TTS {
	//메뉴 테이블의 모든 메뉴를 대상으로 TTS 생성(파이썬 스크립트를 실행함)
	public static void Create() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "tts_run.py");
            pb.inheritIO();
            pb.start().waitFor();//파이썬 실행 시 완료까지 대기함
        } catch (Exception e) {
            System.out.println("TTS 음성파일 생성 중 오류 발생");
            e.printStackTrace();
        }
    }
	 public static void play(String filename) {
	        try {
	            File audioFile = new File("audio_cache/" + filename);//audio_cache = 음성파일 저장 주소
	            if (!audioFile.exists()) {
	                System.out.println("오디오 파일이 존재하지 않습니다: " + filename);
	                return;
	            }

	            //오디오 파일 재생
	            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioIn);
	            clip.start();

	            // 재생 완료까지 대기
	            while (!clip.isRunning())
	                Thread.sleep(10);
	            while (clip.isRunning())
	                Thread.sleep(10);

	            clip.close();
	            audioIn.close();

	        } catch (Exception e) {
	            System.out.println("음성 재생 중 오류 발생:");
	            e.printStackTrace();
	        }
	    }
}
