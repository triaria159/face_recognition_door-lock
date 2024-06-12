import cv2
import os
import numpy as np

# LBPH(Local Binary Patterns Histograms) 인식기 사용
recognizer = cv2.face.LBPHFaceRecognizer_create()
face_detector = cv2.CascadeClassifier('/home/tria/Face/myEnv/opencv-face-recognition/face-detection/haarcascade_frontalface_default.xml')
path = 'dataset'

# 데이터셋의 이미지를 읽고 그레이스케일 값으로 변환한 후 샘플을 반환하는 함수
def getImagesAndLabels(path):
    faceSamples = []
    ids = []

    for file_name in os.listdir(path):
        if file_name.endswith(".jpg"):
            id = file_name.split(".")[1]  # id를 문자열로 유지
            img_path = os.path.join(path, file_name)
            img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)

            faces = face_detector.detectMultiScale(img)

            for (x, y, w, h) in faces:
                faceSamples.append(img[y:y+h, x:x+w])
                ids.append(id)  # id를 문자열로 추가

    return faceSamples, ids

def trainRecognizer(faces, ids):
    unique_ids = list(set(ids))  # 고유한 ID 리스트 생성
    id_to_num = {id: num for num, id in enumerate(unique_ids)}  # ID를 숫자로 매핑
    numeric_ids = [id_to_num[id] for id in ids]  # 각 ID를 매핑된 숫자로 변환

    recognizer.train(faces, np.array(numeric_ids, dtype=np.int32))  # ids 배열을 int32 타입으로 변환
    # 'trainer' 폴더가 없으면 생성
    if not os.path.exists("trainer"):
        os.makedirs("trainer")
    # 모델을 'trainer/trainer.yml'에 저장
    recognizer.write('trainer/trainer.yml')

print("\n [INFO] 얼굴 훈련 중. 몇 초 정도 걸립니다. 기다려 주세요...")
# 얼굴 샘플과 해당하는 레이블을 가져오기
faces, ids = getImagesAndLabels(path)

# 얼굴 샘플과 해당하는 레이블을 사용하여 LBPH 인식기 훈련
trainRecognizer(faces, ids)

# 훈련된 고유 얼굴 수 출력
num_faces_trained = len(set(ids))
print("\n [INFO] {}개의 얼굴이 훈련되었습니다. 프로그램 종료".format(num_faces_trained))
