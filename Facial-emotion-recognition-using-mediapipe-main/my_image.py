import csv
import copy
import itertools
import os
import cv2
import mediapipe as mp

import numpy as np
import tensorflow as tf

# Load the TFLite model and allocate tensors.
interpreter = tf.lite.Interpreter(model_path="C:/hci/Facial-emotion-recognition-using-mediapipe-main/model/keypoint_classifier/face_mesh.tflite")
interpreter.allocate_tensors()

# Get input and output tensors.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

def encode_label(label_name,category):
    for i in category:
        if i == label_name:
            return category.index(i)

def calc_landmark_list(image):
    image_width, image_height = image.shape[1], image.shape[0]

    # Show the original image

    # Preprocess the image
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = image.astype(np.float32) / 255.0
    image = cv2.resize(image, (192, 192))
    image = np.expand_dims(image, axis=0).astype(np.float32)

    # # Show the image
    # cv2.imshow("Preprocessed Image", image[0])
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()

    # Set the tensor to point to this input data
    interpreter.set_tensor(input_details[0]['index'], image)

    # Run inference
    interpreter.invoke()

    # Get the output tensor
    output_data = interpreter.get_tensor(output_details[0]['index'])

    landmark_point = []

    for i in range(output_data.shape[3] // 3):
        landmark_x = output_data[0, 0, 0, i * 3]
        landmark_y = output_data[0, 0, 0, i * 3 + 1]

        landmark_point.append([landmark_x, landmark_y])

    # print(len(landmark_point))
    return landmark_point

def pre_process_landmark(landmark_list):
    temp_landmark_list = copy.deepcopy(landmark_list)

    # Convert to relative coordinates
    base_x, base_y = 0, 0
    for index, landmark_point in enumerate(temp_landmark_list):
        if index == 0:
            base_x, base_y = landmark_point[0], landmark_point[1]

        temp_landmark_list[index][0] = temp_landmark_list[index][0] - base_x
        temp_landmark_list[index][1] = temp_landmark_list[index][1] - base_y

    # Convert to a one-dimensional list
    temp_landmark_list = list(itertools.chain.from_iterable(temp_landmark_list))

    # # Remove last 20 elements
    # temp_landmark_list = temp_landmark_list[:-20]

    # Normalization
    max_value = max(list(map(abs, temp_landmark_list)))

    # If max_value is 0, return None
    if max_value == 0:
        print("Max value is zero. Skipping normalization.")
        return None

    def normalize_(n):
        return n / max_value

    temp_landmark_list = list(map(normalize_, temp_landmark_list))

    return temp_landmark_list

def logging_csv(number, landmark_list):
    if 0 <= number <= 4:
        csv_path = 'C:/hci/Facial-emotion-recognition-using-mediapipe-main/model/keypoint_classifier/keypoint.csv'
        with open(csv_path, 'a', newline="") as f:
            writer = csv.writer(f)
            writer.writerow([number, *landmark_list])
    return


root = "C:/hci/Facial-emotion-recognition-using-mediapipe-main/model/keypoint_classifier/face4"
IMAGE_FILES = []
category = ['Happy','Neutral','Sad','Surprise']
for path, subdirs, files in os.walk(root):
    for name in files:
        IMAGE_FILES.append(os.path.join(path, name))


for idx, file in enumerate(IMAGE_FILES):
    label_name = file.rsplit("/", 1)[-1]
    label_name = label_name.rsplit("\\", 1)[0]
    label_name = label_name.split("\\", 1)[1]
    label = encode_label(label_name, category)
    image = cv2.imread(file)
    image = cv2.flip(image, 1)  # Mirror display
    debug_image = copy.deepcopy(image)

    # Landmark calculation
    landmark_list = calc_landmark_list(debug_image)

    # Conversion to relative coordinates / normalized coordinates
    pre_processed_landmark_list = pre_process_landmark(landmark_list)

    # Write to the dataset file
    logging_csv(label, pre_processed_landmark_list)