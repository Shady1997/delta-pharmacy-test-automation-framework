package com.pharmacy.automation.utils;

import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder {
    private static final Logger logger = LogManager.getLogger(VideoRecorder.class);
    private ScreenRecorder screenRecorder;
    private String testName;

    public void startRecording(String testName) throws Exception {
        this.testName = testName;

        File videoDir = new File(FrameworkConstants.VIDEOS_PATH);
        if (!videoDir.exists()) {
            videoDir.mkdirs();
        }

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        Format fileFormat = new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI);
        Format screenFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
                ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                QualityKey, 1.0f,
                KeyFrameIntervalKey, 15 * 60);
        Format mouseFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                FrameRateKey, Rational.valueOf(30));

        screenRecorder = new ScreenRecorder(gc, null,
                fileFormat, screenFormat, mouseFormat, null, videoDir);

        screenRecorder.start();
        logger.info("Video recording started for test: {}", testName);
    }

    public void stopRecording(boolean testPassed) throws Exception {
        if (screenRecorder != null) {
            screenRecorder.stop();
            logger.info("Video recording stopped for test: {}", testName);

            if (testPassed && FrameworkConstants.RECORD_FAILED_TESTS_ONLY) {
                File videoFile = screenRecorder.getCreatedMovieFiles().get(0);
                if (videoFile.exists()) {
                    videoFile.delete();
                    logger.info("Video deleted for passed test: {}", testName);
                }
            }
        }
    }
}