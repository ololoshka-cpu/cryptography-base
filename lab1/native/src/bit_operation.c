#include "../include/bit_operation.h"

uint8_t get_bit(const uint8_t *block, size_t index) {
    return (block[index / 8] >> (7 - index % 8)) & 1u;
}

void set_bit(uint8_t* block, size_t index, uint8_t bit) {
    uint8_t mask = 1u << (7 - index % 8);
    if (bit) {
        block[index / 8] |= mask;
    } else {
        block[index / 8] &= (~mask);
    }
}

void print_byte_array_binary(const uint8_t* data, size_t len) {
    for (size_t i = 0; i < len; i++) {
        print_byte_binary(data[i]);
        printf(" ");
    }
    printf("\n");
}

void print_byte_binary(uint8_t byte) {
    for (int i = 7; i >= 0; i--) {
        printf("%c", (byte & (1 << i)) ? '1' : '0');
    }
}

void byte_xor(uint8_t *first, uint8_t *second, uint8_t *result, size_t bytes_count) {
    for (size_t i = 0; i < bytes_count; i++) {
        result[i] = first[i] ^ second[i];
    }
}
