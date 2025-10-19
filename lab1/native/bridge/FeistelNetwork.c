#include <jni.h>
#include <stdlib.h>
#include "project_application_core_FeistelNetwork.h"
#include "../include/common.h"
#include "../include/feistel_network.h"



JNIEXPORT jbyteArray JNICALL Java_project_application_core_FeistelNetwork_applyFeistelNetwork
    (JNIEnv *env, jclass cls, jbyteArray plaintext, jobjectArray outer_keys, jlong round_function) {
        uint8_t **inner_keys = (uint8_t **)malloc(sizeof(uint8_t *) * 16);
        for (int i = 0; i < 16; i++) {
            inner_keys[i] = (uint8_t *)malloc(sizeof(uint8_t) * 6);
        }
        uint8_t inner_plaintext[8] = {0};

        for (jsize i = 0; i < 16; ++i) {
            jbyteArray inner = (jbyteArray)(*env)->GetObjectArrayElement(env, outer_keys, i);
            jbyte* bytes = (*env)->GetByteArrayElements(env, inner, NULL);
            memcpy(inner_keys[i], bytes, 6);
            (*env)->ReleaseByteArrayElements(env, inner, bytes, JNI_ABORT);
        }

        jbyte* bytes = (*env)->GetByteArrayElements(env, plaintext, NULL);
        memcpy(inner_plaintext, bytes, 8);
        (*env)->ReleaseByteArrayElements(env, plaintext, bytes, JNI_ABORT);

        uint8_t result[8];
        feistel_network_apply(inner_plaintext, result, inner_keys, 16, 8, (round_function_t)round_function);

        jbyteArray output_result = (*env)->NewByteArray(env, 8);

        (*env)->SetByteArrayRegion(env, output_result, 0, 8, (const jbyte*)result);
        return output_result;
    };