# TempoTalk: Real-Time Speech Rate Analysis Using AI

TempoTalk is an Android application designed to monitor and analyze speech rate using deep learning models. 
The app uses two neural networks to process audio in real-time, providing feedback on the speaking tempo.

![Feature Graphic](https://github.com/v-perfilev/TempoTalk/blob/master/featuregraphic.png)

## Overview
TempoTalk leverages AI to analyze speech in real-time. The app uses two PyTorch-based neural networks:
1. A **Convolutional Neural Network (CNN)** for speech denoising.
2. A **Recurrent Neural Network (RNN)** with an attention mechanism for counting syllables in speech.

This architecture ensures that all processing is performed on-device, providing results while maintaining user privacy.

## Features
- **Real-Time Speech Analysis**: Instant feedback on the speech rate (syllables per second).
- **Offline Processing**: All computations are performed on your device.
- **Customizable Settings**: Adjust syllable rate thresholds, notification preferences, etc.
- **Background Monitoring**: Continues to analyze speech even when running in the background.

## How It Works
TempoTalk combines two neural networks to deliver precise speech rate measurements:

### 1. Speech Denoising
- Based on the [speech_denoiser](https://github.com/v-perfilev/speech_denoiser) project.
- Utilizes a CNN to filter out background noise and isolate speech signals.
- Trained on datasets like Mozilla Common Voice (speech) and UrbanSound8K (environmental noise).

### 2. Syllable Counting
- Uses the [syllable_counter](https://github.com/v-perfilev/syllable_counter) project.
- Employs an RNN with an attention mechanism to count syllables from audio spectrograms.
- Trained to detect syllables in varied speech patterns.

## Dependencies
The following technologies and libraries are used in TempoTalk:

- **Kotlin**: Primary language for Android development.
- **Jetpack Compose**: Modern UI toolkit for Android.
- **PyTorch**: For deep learning models.

## Prerequisites
- **Android Studio** (latest version recommended)
- **Android device** with a functional microphone.

## License
This project is licensed under the MIT License

