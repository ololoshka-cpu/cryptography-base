#include <jni.h>
#include <stdlib.h>
#include "../include/DES/round_function.h"
#include "project_algorythm_des_DESRoundFunction.h"

JNIEXPORT jbyteArray JNICALL Java_project_algorythm_des_DESRoundFunction_applyDesRoundFunction
    (JNIEnv *env, jclass cls, jbyteArray input_block, jbyteArray round_key) {

        jbyte *input_block_bytes = (*env)->GetByteArrayElements(env, input_block, NULL);
        jbyte *round_key_bytes = (*env)->GetByteArrayElements(env, round_key, NULL);

        if (input_block_bytes == NULL || round_key_bytes == NULL) {
            return NULL;
        }

        const uint8_t *input_block_inner = (const uint8_t *)input_block_bytes;
        const uint8_t *round_key_inner = (const uint8_t *)round_key_bytes;

        uint8_t result[4] = {0};

        round_function_des(input_block_inner, round_key_inner, result);

        jbyteArray result_outer = (*env)->NewByteArray(env, 4);
        (*env)->SetByteArrayRegion(env, result_outer, 0, 4, (jbyte *)result);

        (*env)->ReleaseByteArrayElements(env, input_block, input_block_bytes, JNI_ABORT);
        (*env)->ReleaseByteArrayElements(env, round_key, round_key_bytes, JNI_ABORT);

        return result_outer;
        
    };

JNIEXPORT jlong JNICALL Java_project_algorythm_des_DESRoundFunction_getFunctionAddressInLibrary
    (JNIEnv *env, jclass cls) {
        // printf("%p\n", round_function_des);
        return (jlong)(uintptr_t)round_function_des;
    };