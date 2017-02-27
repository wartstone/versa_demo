LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := versa_native
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_SRC_FILES := versa_native.cpp android_fopen.c torchandroid.cpp

LOCAL_LDLIBS += -llog -landroid -L$(LOCAL_PATH)/prebuilts -lluaT -lluajit -lTH -lTHNN  -ltorch -lnnx -limage -ltorchandroid

#LOCAL_SHARED_LIBRARIES = prebuilts/libpng.so

include $(BUILD_SHARED_LIBRARY)

# Add prebuilt libluajit
include $(CLEAR_VARS)
LOCAL_MODULE := libluajit
LOCAL_SRC_FILES := prebuilts/libluajit.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libluaT
include $(CLEAR_VARS)
LOCAL_MODULE := libluaT
LOCAL_SRC_FILES := prebuilts/libluaT.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libTH
include $(CLEAR_VARS)
LOCAL_MODULE := libTH
LOCAL_SRC_FILES := prebuilts/libTH.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libthnn
include $(CLEAR_VARS)
LOCAL_MODULE := libTHNN
LOCAL_SRC_FILES := prebuilts/libTHNN.so
include $(PREBUILT_SHARED_LIBRARY)

######################################
# Add prebuilt libimage
include $(CLEAR_VARS)
LOCAL_MODULE := libimage
LOCAL_SRC_FILES := prebuilts/libimage.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libtorchandroid
include $(CLEAR_VARS)
LOCAL_MODULE := libtorchandroid
LOCAL_SRC_FILES := prebuilts/libtorchandroid.so
include $(PREBUILT_SHARED_LIBRARY)


# Add prebuilt libtorch
include $(CLEAR_VARS)
LOCAL_MODULE := libtorch
LOCAL_SRC_FILES := prebuilts/libtorch.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libpaths
include $(CLEAR_VARS)
LOCAL_MODULE := libpaths
LOCAL_SRC_FILES := prebuilts/libpaths.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libgnustl
include $(CLEAR_VARS)
LOCAL_MODULE := libgnustl_shared
LOCAL_SRC_FILES := prebuilts/libgnustl_shared.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libnnx
include $(CLEAR_VARS)
LOCAL_MODULE := libnnx
LOCAL_SRC_FILES := prebuilts/libnnx.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libppm
include $(CLEAR_VARS)
LOCAL_MODULE := libppm
LOCAL_SRC_FILES := prebuilts/libppm.so
include $(PREBUILT_SHARED_LIBRARY)


# Add prebuilt libsundown
include $(CLEAR_VARS)
LOCAL_MODULE := libsundown
LOCAL_SRC_FILES := prebuilts/libsundown.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libsys
include $(CLEAR_VARS)
LOCAL_MODULE := libsys
LOCAL_SRC_FILES := prebuilts/libsys.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libthreads
include $(CLEAR_VARS)
LOCAL_MODULE := libthreads
LOCAL_SRC_FILES := prebuilts/libthreads.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libthreadsmain
include $(CLEAR_VARS)
LOCAL_MODULE := libthreadsmain
LOCAL_SRC_FILES := prebuilts/libthreadsmain.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libpngs
include $(CLEAR_VARS)
LOCAL_MODULE := libpngs
LOCAL_SRC_FILES := prebuilts/libpngs.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libpng16
include $(CLEAR_VARS)
LOCAL_MODULE := libpng16
LOCAL_SRC_FILES := prebuilts/libpng16.so
include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt liblingshan
include $(CLEAR_VARS)
LOCAL_MODULE := liblingshan
LOCAL_SRC_FILES := prebuilts/liblingshan.so
include $(PREBUILT_SHARED_LIBRARY)

