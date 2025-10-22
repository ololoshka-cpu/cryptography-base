#include "../include/bit_permutation.h"
#include "../include/bit_operation.h"

#include <stdio.h>


void bit_permutation(const uint8_t* input,
                     size_t input_len,
                     size_t* permutation,
                     bool is_reverse_order,
                     bool bit_index_starts_at_1,
                     uint8_t* output,
                     size_t output_len) {
    // printf("length = %d, resSize = %d\n", input_len, output_len);
    for (size_t i = 0; i < output_len; i++) {
        size_t needed_bit = permutation[i];
        if (!bit_index_starts_at_1 && is_reverse_order) {
            needed_bit = input_len - needed_bit - 1;
        } else if (bit_index_starts_at_1 && is_reverse_order) {
            needed_bit = input_len - needed_bit;
        } else if (bit_index_starts_at_1) {
            needed_bit -= 1;
        }
        set_bit(output, i, get_bit(input, needed_bit));
    }
}