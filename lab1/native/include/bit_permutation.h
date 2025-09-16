#include <stdbool.h>
#include <stdint.h>   
#include <stdlib.h>


void bit_permutation(const uint8_t* input,
                     size_t input_len,
                     const size_t* permutation,
                     bool is_reverse_order,
                     bool bit_index_starts_at_1,
                     uint8_t* output,
                     size_t output_len);