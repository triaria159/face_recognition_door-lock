import cv2
import os 
import numpy as np
from picamera2 import Picamera2
import RPi.GPIO as GPIO
import time

# 핀 번호 설정
BUTTON_PIN = 17  # 스위치 연결 핀
DOOR_LOCK_PIN = 18  # 도어락 제어 핀

# 경고 메시지 비활성화
GPIO.setwarnings(False)

# GPIO 초기화
GPIO.cleanup()  # 초기화해서 모든 설정을 초기 상태로 되돌림

# GPIO 설정
GPIO.setmode(GPIO.BCM)
GPIO.setup(BUTTON_PIN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)  # 풀다운 저항 사용
GPIO.setup(DOOR_LOCK_PIN, GPIO.OUT)
# 파라미터
exit_flag = False
id = 0
font = cv2.FONT_HERSHEY_COMPLEX
height=1
boxColor=(0,0,255)      #BGR- 빨강
nameColor=(255,255,255) #BGR- 흰색
confColor=(255,255,0)   #BGR- 노랑

face_detector=cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read('trainer/trainer.yml')
# id와 관련된 이름
names = ['None', 'jimin', 'test']

# PiCamera2 객체 인스턴스 생성
cam = Picamera2()
## 실시간 비디오 캡처 초기화 및 시작
# 카메라 미리보기 해상도 설정
cam.preview_configuration.main.size = (640, 360)
cam.preview_configuration.main.format = "RGB888"
cam.preview_configuration.controls.FrameRate=30
cam.preview_configuration.align()
cam.configure("preview")
cam.start()

def unlock_door():
    # 도어락을 여는 함수
    print("도어락 열림")
    GPIO.output(DOOR_LOCK_PIN, GPIO.HIGH)
    time.sleep(2)  # 도어락이 열리는 시간 (2초 동안 열림 상태 유지)
    GPIO.output(DOOR_LOCK_PIN, GPIO.LOW)
    print("도어락 닫힘")

while True:
    # 카메라에서 프레임 캡처
    frame=cam.capture_array()

    # 프레임을 BGR에서 그레이스케일로 변환
    frameGray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # 4개의 요소(x, y 좌표, 폭, 높이)를 가진 얼굴 배열 생성
    faces = face_detector.detectMultiScale(
            frameGray,      # 그레이스케일 프레임
            scaleFactor=1.1,# 이미지 크기를 각 이미지 스케일에서 10%씩 감소
            minNeighbors=5, # 후보 사각형이 유지되기 위한 최소 이웃 수
            minSize=(150, 150)# 최소 객체 크기. 이 크기보다 작은 객체는 무시됨
            )
    for(x,y,w,h) in faces:
        namepos=(x+5,y-5) # 상자에서 오른쪽으로, 위로 이동
        confpos=(x+5,y+h-5) # 상자 내부에서 오른쪽으로, 위로 이동
        # 탐지된 얼굴 주위에 경계 상자 생성
        cv2.rectangle(frame, (x,y), (x+w,y+h), boxColor, 3) # 5개 매개변수 - 프레임, 좌상단 좌표, 우하단 좌표, 상자 색상, 두께

        # recognizer.predict() 메서드는 ROI를 입력으로 받아
        # 주어진 얼굴 영역에 대한 예측된 레이블(id) 및 신뢰도 점수를 반환
        
        id, confidence = recognizer.predict(frameGray[y:y+h,x:x+w])
        
        # 신뢰도가 100보다 작으면 완벽한 일치로 간주
        if confidence >= 50:
            id = names[id]
            confidence = f"{100 - confidence:.0f}%"
            unlock_door()  # 도어락 열기
            exit_flag = True  # 종료 플래그 설정
            break  # 내부 루프 탈출
        else:
            id = "unknown"
            confidence = f"{100 - confidence:.0f}%"

        # 인식된 사람의 이름과 신뢰도 표시
        cv2.putText(frame, str(id), namepos, font, height, nameColor, 2)
        cv2.putText(frame, str(confidence), confpos, font, height, confColor, 1)

    if exit_flag:
        break  # 메인 루프 탈출

    # 사용자에게 실시간 캡처 출력 표시
    cv2.imshow('Raspi Face Recognizer',frame)

    # 키 이벤트를 30밀리초 동안 대기하고 'ESC' 또는 'q'가 눌리면 종료
    key = cv2.waitKey(100) & 0xff
    # 키 코드 확인
    if key == 27:  # ESC 키
        break
    elif key == 113:  # q 키
        break

# 카메라 해제 및 모든 창 닫기
print("\n [INFO] 프로그램 종료 및 정리")
cam.stop()
cv2.destroyAllWindows()
GPIO.cleanup()