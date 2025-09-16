#include "../include/round_function.h"

void round_function(uint8_t *r_block, uint8_t *round_key, uint8_t *res) {
    uint8_t expanded_r_block[E_TABLE_SIZE] = {0};
    uint8_t xor_r_block[E_TABLE_SIZE] = {0};
    bit_permutation(r_block, 32, E_TABLE, false, true, expanded_r_block, 48);
    byte_xor(expanded_r_block, round_key, xor_r_block, 6);
    for (int i = 0; i < S_BLOCK_COUNT; i++) {
        res[i / 2] |= s_transform(get_input_for_s_transform(xor_r_block, i), i) << (4 * ((i + 1) % 2));
    }
}

uint8_t get_input_for_s_transform(uint8_t *src, int s_number) {
    uint8_t res = 0;
    for (int i = s_number * 6; i < (s_number + 1) * 6; i++) {
        res = res << 1;
        res |= get_bit(src, i);
    }
    return res;
}

uint8_t s_transform(uint8_t src, int s_number) {
    uint8_t row = (src & 0b100000) >> 4 | (src & 0b000001);
    uint8_t column = (src & 0b011110) >> 1;
    return S_BLOCKS[s_number][(row << 4) + column];
}