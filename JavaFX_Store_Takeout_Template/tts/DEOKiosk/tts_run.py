import os
import torch
import mysql.connector


# 보안 정책 우회: xtts 모델 config 및 audio config 등록
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import XttsAudioConfig, XttsArgs
from TTS.config.shared_configs import BaseDatasetConfig
from torch.serialization import add_safe_globals

add_safe_globals([
    XttsConfig,
    XttsAudioConfig,
    BaseDatasetConfig,
    XttsArgs,
])

# weight_norm 경로 문제 수동 패치
try:
    from torch.nn.utils import weight_norm
    import TTS.vocoder.models.hifigan_generator as hifi_module
    hifi_module.weight_norm = weight_norm
except Exception as e:
    print("[Warning] weight_norm 패치 실패:", e)

# 3. TTS 모델 초기화
from TTS.api import TTS
tts = TTS(model_name="tts_models/multilingual/multi-dataset/xtts_v2", gpu=False)

# 4. 설정 경로
speaker_wav_path = r"C:\Users\범서진\Downloads\JavaFX_Store_Takeout_Template\tts\DEOKiosk\sample_speaker.wav"
output_dir = "audio_cache"
os.makedirs(output_dir, exist_ok=True)

# 5. DB 연결 설정
db_config = {
    'host': 'localhost',
    'user': 'DEOKiosk',
    'password': '1234',
    'database': 'kiosk',
    'port': 3306
}

# 6. DB에서 메뉴 정보 불러와 TTS 생성
try:
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()
    cursor.execute("SELECT Name, AudioGuide FROM Menu")
    rows = cursor.fetchall()

    for name, filename in rows:
        output_path = os.path.join(output_dir, filename)
        if not os.path.exists(output_path):
            print(f"[Create] {filename}")
            tts.tts_to_file(
                text=name,
                speaker_wav=speaker_wav_path,
                language="ko",
                file_path=output_path
            )
        else:
            print(f"[Skip] {filename} (Already exists)")

    print("\n[Done] 모든 음성 파일 생성 완료.")

except Exception as e:
    print("Warning : ")
    print(e)

finally:
    if 'cursor' in locals(): cursor.close()
    if 'conn' in locals() and conn.is_connected(): conn.close()