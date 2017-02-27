LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := lingshan
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_SRC_FILES := test.c

LOCAL_LDLIBS += -llog -landroid -L$(LOCAL_PATH)/prebuilts -lluajit

include $(BUILD_SHARED_LIBRARY)

# Add prebuilt libluajit
#include $(CLEAR_VARS)
#LOCAL_MODULE := libluajit
#LOCAL_SRC_FILES := prebuilts/libluajit.so
#include $(PREBUILT_SHARED_LIBRARY)