# -*- coding: utf-8 -*-
import speech_recognition as sr
import sys
import io

# 표준 출력 인코딩을 UTF-8로 강제 설정
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def recognize_speech():
    recognizer = sr.Recognizer()
    mic = sr.Microphone()

    print("🎤 음성을 인식합니다... (녹음 중)")

    with mic as source:
        recognizer.adjust_for_ambient_noise(source)
        audio = recognizer.listen(source)

    try:
        text = recognizer.recognize_google(audio, language="ko-KR")
        print(f"인식된 내용: {text}")
    except sr.UnknownValueError:
        print("음성을 인식할 수 없습니다.")
    except sr.RequestError:
        print("음성 인식 서비스에 접근할 수 없습니다.")

if __name__ == "__main__":
    recognize_speech()