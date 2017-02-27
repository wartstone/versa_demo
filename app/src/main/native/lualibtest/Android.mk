LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := pngs
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include $(LOCAL_PATH)/include/TH
LOCAL_SRC_FILES := test.c

LOCAL_LDLIBS += -llog -landroid
#LOCAL_LDLIBS += -llog -landroid -L$(LOCAL_PATH)/prebuilts -lluaT -lluajit -lTH -lTHNN  -ltorch -lnnx -limage -ltorchandroid -ljpeg_func

include $(BUILD_SHARED_LIBRARY)