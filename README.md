# ImageDetector App README

## Overview

This README provides an overview of the ImageDetector application and its implementation details.

## App Setup

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Ensure that necessary dependencies are installed and configured.
4. Run the application on an Android device or emulator.
5. 
### MainActivity

The `MainActivity` class handles image processing and prediction using a pre-trained MobileNet model. Here's a breakdown of its key parts:

- **JNI (Java Native Interface)**: 
  - Declares the `helloWorld()` function as an external function, likely implemented in native code (C or C++).
  
- **Initialization**:
  - The `onCreate` method initializes UI components and sets up click listeners for buttons.
  - Loads labels from the `labels.txt` file located in the assets directory.
  
- **Image Processing**:
  - `onActivityResult` method handles image selection.
  - Selected image is resized to 224x224 pixels and displayed in the UI.
  
- **Model Inference**:
  - When the user clicks the "Predict" button:
    - Selected image is loaded into a TensorImage object and processed using an ImageProcessor.
    - Processed image is passed to the MobileNet model for inference.
    - Output probabilities are obtained from the model, and the highest probability label is displayed as the prediction result.
  
- **JNI Implementation**:
  - The native method `helloWorld()` is declared in the companion object and used to test JNI setup, returning a string from native code.
  
This code demonstrates integration of a TensorFlow Lite model for image classification into an Android app using Kotlin and JNI.

### C++ Code and CMakeLists.txt

- The provided C++ code includes a `helloWorld` function returning "Predictions...". 
- Integrated into the Android application via JNI.
- The `CMakeLists.txt` file sets up build configuration for the C++ code, creating a shared library and linking necessary libraries.

### Model Name: mobilenet_v1_1.0_224_quant
### Model Type: TensorFlow Lite
### Input Size: 224x224 pixels
### Labels: 1001 labels from the `labels.txt` file

### AndroidManifest.xml

- Defines configuration for the Android application.
- Specifies application and activity settings, including backup rules, icons, theme, and launch behavior.

## Conclusion

The ImageDetector app sounds like a versatile tool for image classification tasks! Leveraging a pre-trained MobileNet model trained on a deep learning CNN architecture is a smart choice for achieving accurate predictions. The integration of native code through JNI adds another layer of functionality, potentially improving performance and expanding the app's capabilities. With its intuitive interface and efficient model inference, users can quickly and reliably classify images for a variety of purposes. Whether for personal or professional use, this app seems like a valuable asset in the realm of image analysis.
