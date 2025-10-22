#include <stdlib.h>
#include <string.h>
#include "project_application_core_NativeFunctions.h"
#include "../include/bit_permutation.h"
#include "../include/bit_operation.h"

JNIEXPORT jbyteArray JNICALL Java_project_application_core_NativeFunctions_bitPermutation
    (JNIEnv *env, jclass cls, jbyteArray input, jint length, jlongArray permutation,
     jint permLength, jboolean isReverse, jboolean isStartAtOne, jint resSize) {

        uint8_t *inner_input = (uint8_t *)malloc(sizeof(uint8_t) * length / 8);
        uint8_t *result = (uint8_t *)malloc(sizeof(uint8_t) * length / 8);

        jlong *longs_permutation = (*env)->GetLongArrayElements(env, permutation, NULL);
        jsize len = (*env)->GetArrayLength(env, permutation);
        size_t *inner_permutation = (size_t *)malloc(sizeof(size_t) * len);

        jbyte* bytes_input = (*env)->GetByteArrayElements(env, input, NULL);
        jbyte* bytes_permutation = (*env)->GetByteArrayElements(env, permutation, NULL);

        memcpy(inner_input, bytes_input, length / 8);
        for (int i = 0; i < len; i++) {
            inner_permutation[i] = (size_t)longs_permutation[i];
        }        

        // for (int i = 0; i < len; i++) {
        //     printf("%d ", inner_permutation[i]);
        // }

        bit_permutation(inner_input, (size_t)length, inner_permutation, (bool)isReverse, (bool)isStartAtOne, result, (size_t)resSize);

        jbyteArray output_result = (*env)->NewByteArray(env, length / 8);
        
        (*env)->SetByteArrayRegion(env, output_result, 0, length / 8, (const jbyte*)result);
        (*env)->ReleaseByteArrayElements(env, input, bytes_input, JNI_ABORT);
        (*env)->ReleaseByteArrayElements(env, permutation, bytes_permutation, JNI_ABORT);
        (*env)->ReleaseLongArrayElements(env, permutation, longs_permutation, JNI_ABORT);
        free(inner_input);
        free(inner_permutation);
        free(result);
        
        return output_result;
    };