#include "../include/key_schedule.h"

#include <stdio.h>

void round_left_shift(uint8_t *src, size_t shifts, size_t src_size);

void print_left_key(uint8_t *src);
void print_right_key(uint8_t *src);

void generate_round_keys(const uint8_t input_key[KEY_BYTE_SIZE], uint8_t round_keys[NUM_ROUNDS][ROUND_KEY_SIZE]) {
    size_t shifts_count[] = {
        1, 1, 2, 2,
        2, 2, 2, 2,
        1, 2, 2, 2,
        2, 2, 2, 1
    };

    const size_t PC1_TABLE[56] = {
        57, 49, 41, 33, 25, 17, 9,
         1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
         7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
    };

    const size_t PC2_TABLE[48] = {
        14, 17, 11, 24,  1,  5,
         3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8,
        16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    uint8_t temp_key[7] = {0};
    bit_permutation(input_key, 64, PC1_TABLE, false, true, temp_key, 56);

        // printf("C%2d: ", 0);
        // print_left_key(temp_key);
        // printf("D%2d: ", 0);
        // print_right_key(temp_key);
        // printf("\n");

    for (int i = 0; i < NUM_ROUNDS; i++) {
        round_left_shift(temp_key, shifts_count[i], 7);
        // printf("C%2d: ", i + 1);
        // print_left_key(temp_key);
        // printf("D%2d: ", i + 1);
        // print_right_key(temp_key);
        // printf("\n");
        bit_permutation(temp_key, 56, PC2_TABLE, false, true, round_keys[i], 48);
        // printf("K%d = ", i + 1);
        // print_byte_array_binary(round_keys[i], 6);
    }

}

void round_left_shift(uint8_t *src, size_t shifts, size_t src_size) {
    for (size_t i = 0; i < shifts; i++) {
        uint8_t bit_0 = get_bit(src, 0);
        uint8_t bit_28 = get_bit(src, 28);
        uint8_t lower_bit = 0b0;
        
        for (size_t i = 0; i < src_size; i++) {
            lower_bit = get_bit(src, (i + 1) * 8);
            src[i] = src[i] << 1 | lower_bit;
        }

        set_bit(src, 27, bit_0);
        set_bit(src, 55, bit_28);
    }
}

void print_left_key(uint8_t *src) {
    for (int i = 0; i < 28; i++) {
        printf("%hhu", get_bit(src, i));
    }
    printf("\n");
}

void print_right_key(uint8_t *src) {
    for (int i = 28; i < 56; i++) {
        printf("%hhu", get_bit(src, i));
    }
    printf("\n");
}


