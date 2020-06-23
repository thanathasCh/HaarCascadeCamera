Haar Cascade Camera on Android application
=========
I have developed this project back in 2018 when I was semohpore. At the moment I just leant how to use haar cascade model to perform face detection, so I was very exited about that.
At that time, I only knew how to develop android application with Kotlin, so I want to integrate the android knowlege with my new computer vision knowlege. The project consists of 3 parts.

1. Reading Haar Cascade model into the android environment.
2. Apply the haar cascade model to each frame from the camera.
3. Drawing the rectangle on the image and send it back to the user.

I have not touch this project since 2018, so I might not be able to remember every detail of it, but I can remember that I used the android extension from openCV and use it to do the
image processing in android enviroment.



How to run:
------
1. You will need the IDE to develop android application. I would not give any installation step over here since I assume you already know how to do those things, and there are plenty of
installtion tutorial that you can search for. However, I do recommend you to use Android Studio, since it will automaticall detect the configuration that I made between C++ library and Java.
The Android Studio IDE will deal those thing for you. You can try to do those things by yourself, but I would not recommend.

2. After the IDE has configurated everything, you will have to edit the following code\

```
final int REAR_CAMERA = 0;
final int FRONT_CAMERA =  1;
final int MAX_WIDTH = 320;
final int MAX_HEIGHT = 240;
```

First is the MAX_WIDTH and MAX_HEIGHT, they are the default width and height of your android camera. It may throw an error if the resolution is not compatable.



```
private void initCameraView() {
    cameraView = findViewById(R.id.cameraView);
    cameraView.setCameraIndex(FRONT_CAMERA);
    cameraView.setCvCameraViewListener(this);
    cameraView.enableView();
    cameraView.enableFpsMeter();
    cameraView.setMaxFrameSize(MAX_WIDTH, MAX_HEIGHT);
}
```

Socond is the `cameraView.setCameraIndex()`, your android phone has 2 cameras that the program can use either it is the rear camera or front camera. You can choose to use
either of them by putting parameter `REAR_CAMERA` or `FRONT_CAMERA`



3. Click run!!



Screenshot
------
These are the screenshot from the application, it will draw the rectangle on the scrren and the fps. There might be the problem when the haar cascade model can not detect any faces
or detect something that is not face. It is pretty normal with haar cascade model. You can try to change some parameters to improve that in the following code in `onCameraFrame` function.

`detector.detectMultiScale(org_img, objVectors, 1.8, 5);`

![](Image Url)