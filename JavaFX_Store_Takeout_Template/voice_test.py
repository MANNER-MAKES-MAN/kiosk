import os
import sys
import json
import queue
import sounddevice as sd
import vosk

# === 설정 ===
SAMPLE_RATE = 16000
MODEL_PATH = r"JavaFX_Store_Takeout_Template\src\app\vosk-model-small-ko-0.22"

# === 제한 단어 ===
MENU_GRAMMAR = ["아메리카노", "카페라떼", "아이스티"]
QTY_GRAMMAR = [
    "한개","한 개","1개","하나",
    "한잔","한 잔","1잔",
    "두개","두 개","2개","둘",
    "셋","세 개","3개",
    "넷","네 개","4개",
    "다섯개","다섯 개","5개","다섯",
    "여섯개","여섯 개","6개","여섯",
    "일곱개","일곱 개","7개","일곱",
    "여덟개","여덟 개","8개","여덟",
    "아홉개","아홉 개","9개","아홉"
]
ACTION_GRAMMAR = ["없음", "없어요", "주문 완료", "완료"]
ALL_GRAMMAR = MENU_GRAMMAR + QTY_GRAMMAR + ACTION_GRAMMAR

# === 모델 로딩 ===
if not os.path.isdir(MODEL_PATH):
    print(f"모델 경로가 존재하지 않습니다: {MODEL_PATH}", file=sys.stderr)
    sys.exit(1)

model = vosk.Model(MODEL_PATH)
recognizer = vosk.KaldiRecognizer(model, SAMPLE_RATE, json.dumps(ALL_GRAMMAR, ensure_ascii=False))
q = queue.Queue()

def callback(indata, frames, time, status):
    if status:
        print(f"오디오 오류: {status}", file=sys.stderr)
    q.put(bytes(indata))

def main():
    print("이제 말씀하셔도 됩니다...")
    with sd.RawInputStream(samplerate=SAMPLE_RATE, blocksize=8000, dtype="int16", channels=1, callback=callback):
        while True:
            data = q.get()
            if recognizer.AcceptWaveform(data):
                result = json.loads(recognizer.Result())
                text = result.get("text", "").strip()
                if text:
                    print(text)  # Java가 읽을 텍스트 출력
                    break

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        sys.exit(0)