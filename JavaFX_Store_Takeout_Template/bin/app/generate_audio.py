import os
import json
import torch
from TTS.api import TTS
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import XttsAudioConfig, XttsArgs
from TTS.config.shared_configs import BaseDatasetConfig

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
VOSK_MODEL  = os.path.abspath(os.path.join(CURRENT_DIR, "vosk-model-small-ko-0.22"))
SPEAKER_WAV  = os.path.abspath(os.path.join(CURRENT_DIR, "sample_speaker.wav"))
OUTPUT_DIR   = os.path.abspath(os.path.join(CURRENT_DIR, "audio_cache"))
os.makedirs(OUTPUT_DIR, exist_ok=True)

MENU_GRAMMAR = ["아메리카노", "카페라떼", "아이스티", "핫초코", "에이드", "스무디"]
STATIC = {
    "ask_menu": "원하시는 메뉴를 하나만 말해주세요",
    "ask_more": "다른 메뉴를 추가 하시겠습니까? 없으면 주문 완료, 있으면 메뉴를 하나만 말씀해주세요",
    "ask_done": "주문이 완료되었습니다. 이제 결제를 진행해주세요",
}

#  수량별 표준 읽기 (1개~9개) 
QTY_CANON = {i: f"{i}개" for i in range(1, 10)}

torch.serialization.add_safe_globals([
    XttsConfig, XttsAudioConfig, XttsArgs, BaseDatasetConfig
])
tts = TTS(model_name="tts_models/multilingual/multi-dataset/xtts_v2", gpu=False)

prompts_meta = {}
def tts_to_cache(key: str, text: str):
    wav_path = os.path.join(OUTPUT_DIR, f"{key}.wav")
    if not os.path.isfile(wav_path):
        print(f"Generating {key}.wav …")
        tts.tts_to_file(text=text,
                        speaker_wav=SPEAKER_WAV,
                        language="ko",
                        file_path=wav_path)
    prompts_meta[key] = {"text": text, "wav": wav_path}

for k, txt in STATIC.items():
    tts_to_cache(k, txt)

for menu in MENU_GRAMMAR:
    tts_to_cache(f"menu_{menu}", menu)

tts_to_cache("ask_qty", "몇 개 주문하시겠습니까?")

for n, txt in QTY_CANON.items():
    tts_to_cache(f"confirm_qty_{n}", f"{txt} 주문이 추가되었습니다")

with open(os.path.join(OUTPUT_DIR, "prompts.json"), "w", encoding="utf-8") as f:
    json.dump(prompts_meta, f, ensure_ascii=False, indent=2)

print("오디오 생성 완료")
