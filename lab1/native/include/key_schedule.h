#ifndef KEY_SCHEDULE_H
#define KEY_SCHEDULE_H

#include <stdint.h>
#include <stddef.h>
#include "bit_permutation.h"
#include "bit_operation.h"

#define NUM_ROUNDS 16
#define ROUND_KEY_SIZE 6 
#define KEY_BYTE_SIZE 8


void generate_round_keys(const uint8_t input_key[8], uint8_t round_keys[NUM_ROUNDS][ROUND_KEY_SIZE]);
void round_left_shift(uint8_t *src, size_t shifts, size_t src_size);

#endif