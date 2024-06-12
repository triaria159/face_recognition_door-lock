import cv2
import os
from picamera2 import Picamera2

# 상수 설정
COUNT_LIMIT = 30
POS=(30,60)  # 텍스트 오버레이의 좌상단 위치
FONT=cv2.FONT_HERSHEY_COMPLEX # 텍스트 오버레이용 글꼴 유형
HEIGHT=1.5  # 글꼴 크기
TEXTCOLOR=(0,0,255)  # 텍스트 색상 (BGR 형식 - 빨강)
BOXCOLOR=(255,0,255) # 상자 색상 (BGR 형식 - 파랑)
WEIGHT=3  # 글꼴 두께
FACE_DETECTOR=cv2.CascadeClassifier('/home/tria/Face/myEnv/opencv-face-recognition/face-detection/haarcascade_frontalface_default.xml')

# 각 사람에 대해 숫자 얼굴 ID 입력
face_id = input('\n---- 사용자 ID를 입력하고 <return>을 누르세요 ----')
print("\n [INFO] 얼굴 캡처 초기화 중. 카메라를 보고 기다리세요!")

# PiCamera2 객체 인스턴스 생성
cam = Picamera2()
## 카메라 미리보기 해상도 설정
cam.preview_configuration.main.size = (640, 360)
cam.preview_configuration.main.format = "RGB888"
cam.preview_configuration.controls.FrameRate=30
cam.preview_configuration.align()
cam.configure("preview")
cam.start()

count=0

while True:
    # 카메라에서 프레임 캡처
    frame=cam.capture_array()
    # 촬영한 이미지 수 표시
    cv2.putText(frame,'Count:'+str(int(count)),POS,FONT,HEIGHT,TEXTCOLOR,WEIGHT)

    # 프레임을 BGR에서 그레이스케일로 변환
    frameGray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # 얼굴을 탐지하여 DS faces 배열 생성 (4개의 요소 - x, y 좌표 (좌상단), 너비, 높이)
    faces = FACE_DETECTOR.detectMultiScale( # detectMultiScale은 4개의 매개변수를 가짐
            frameGray,      # 탐지할 그레이스케일 프레임
            scaleFactor=1.1,# 이미지 크기가 각 스케일마다 얼마나 줄어드는지 - 10% 감소
            minNeighbors=5, # 각 후보 사각형이 유지되기 위해 필요한 이웃 개수
            minSize=(30, 30)# 최소 객체 크기. 이 크기보다 작은 객체는 무시됨.
    )
    for (x,y,w,h) in faces:
        # 탐지된 얼굴에 경계 상자 생성
        cv2.rectangle(frame, (x,y), (x+w,y+h), BOXCOLOR, 3) # 5개의 매개변수 - 프레임, 좌상단 좌표, 우하단 좌표, 상자 색상, 두께
        count += 1 # 이미지 수 증가

        # dataset 폴더가 없으면 생성
        if not os.path.exists("dataset"):
            os.makedirs("dataset")
        # 동일한 파일이 없는 경우에만 캡처된 그레이스케일 이미지를 dataset 폴더에 저장
        file_path = os.path.join("dataset", f"User.{face_id}.{count}.jpg")
        if os.path.exists(file_path):
            # 기존 파일을 "old_dataset" 폴더로 이동
            old_file_path = file_path.replace("dataset", "old_dataset")
            os.rename(file_path, old_file_path)
        # 이전 이미지를 이동한 후 새로운 이미지 작성
        cv2.imwrite(file_path, frameGray[y:y+h, x:x+w])

    # 원본 프레임을 사용자에게 표시
    cv2.imshow('FaceCapture', frame)
    # 30밀리초 동안 키 이벤트를 기다리고 'ESC' 또는 'q'가 눌리면 종료
    key = cv2.waitKey(100) & 0xff
    # 키코드 확인
    if key == 27:  # ESCAPE 키
        break
    elif key == 113:  # q 키
        break
    elif count >= COUNT_LIMIT: # COUNT_LIMIT만큼의 얼굴 샘플을 찍고 비디오 캡처 중지
        break

# 카메라 릴리스 및 모든 창 닫기
print("\n [INFO] 프로그램 종료 및 정리 중")
cam.stop()
cv2.destroyAllWindows()
