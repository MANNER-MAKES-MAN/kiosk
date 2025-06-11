import os, json, queue, sys, sounddevice as sd, soundfile as sf, vosk
import sys
sys.stdout.reconfigure(encoding="utf-8")

# vosk.SetLogLevel(-1)
CUR = os.path.dirname(os.path.abspath(__file__))
MODEL = os.path.join(CUR, "vosk-model-small-ko-0.22")
OUT   = os.path.join(CUR, "audio_cache")
PROM  = os.path.join(OUT, "prompts.json")

assert os.path.isdir(MODEL) and os.path.isfile(PROM)

MENU_TOKENS = {
    "아메리카노": "아메리카노",
    "카페라떼"  : "카페라떼",
    "아이스티"  : "아이스티",
    "핫초코"    : "핫초코",
    "에이드"    : "에이드",
    "스무디"    : "스무디",
}
MENU_GRAMMAR = list(MENU_TOKENS.keys())

QTXT = ["한개","1개","하나",
        "두개","2개","둘",
        "세개","3개","셋",
        "네개","4개","넷",
        "다섯개","5개","다섯",
        "여섯개","6개","여섯",
        "일곱개","7개","일곱",
        "여덟개","8개","여덟",
        "아홉개","9개","아홉"]
QMAP = {**{v:"1개" for v in ("한개","1개","하나")},
        **{v:"2개" for v in ("두개","2개","둘")},
        **{v:"3개" for v in ("세개","3개","셋")},
        **{v:"4개" for v in ("네개","4개","넷")},
        **{v:"5개" for v in ("다섯개","5개","다섯")},
        **{v:"6개" for v in ("여섯개","6개","여섯")},
        **{v:"7개" for v in ("일곱개","7개","일곱")},
        **{v:"8개" for v in ("여덟개","8개","여덟")},
        **{v:"9개" for v in ("아홉개","9개","아홉")}}

ACT_GRAM = MENU_GRAMMAR + ["없음","없어요","주문 완료","완료"]

with open(PROM, encoding="utf-8") as fp:
    PROMPTS = json.load(fp)

def play(key:str):
    meta = PROMPTS[key]; data, sr = sf.read(meta["wav"], dtype="int16")
    print(meta["text"]); sd.play(data, sr); sd.wait()

model = vosk.Model(MODEL)
a_q   = queue.Queue()
def cb(indata, frames, time, status):
    if status: print(status, file=sys.stderr)
    a_q.put(bytes(indata))

def listen(rec, gramm, norm=None):
    while True:
        if rec.AcceptWaveform(a_q.get()):
            t = json.loads(rec.Result()).get("text","").strip()
            if t in gramm: return norm.get(t,t) if norm else t

def main():
    print("[Python] main 시작", flush=True)
    with sd.RawInputStream(samplerate=16000, blocksize=8000,
                           dtype="int16", channels=1, callback=cb):
        print("[Python] 마이크 입력 스트림 시작됨", flush=True)

        rec_m = vosk.KaldiRecognizer(model,16000,
                   json.dumps(MENU_GRAMMAR, ensure_ascii=False))

        play("ask_menu")
        print("[Python] 메뉴 음성 대기 중...", flush=True)
        token = listen(rec_m, MENU_GRAMMAR)
        print(f"[Python] 메뉴 인식 결과: {token}", flush=True)
        menu  = MENU_TOKENS[token]

        while True:
            play(f"menu_{menu}"); play("ask_qty")
            rec_q = vosk.KaldiRecognizer(model,16000,
                       json.dumps(QTXT, ensure_ascii=False))
            qtxt  = listen(rec_q, QTXT, QMAP)
            qty   = int(qtxt[0])
            play(f"confirm_qty_{qty}")

            print(json.dumps(
            {"menu": menu, "qty": qty},
            ensure_ascii=False,
            separators=(',',':')
        ), flush=True)

            play("ask_more")
            rec_a = vosk.KaldiRecognizer(model,16000,
                       json.dumps(ACT_GRAM, ensure_ascii=False))
            act = listen(rec_a, ACT_GRAM)
            if act in MENU_GRAMMAR: menu = MENU_TOKENS[act]
            else: play("ask_done"); break

if __name__ == "__main__":
    try:   main()
    except KeyboardInterrupt: pass
