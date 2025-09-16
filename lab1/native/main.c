#include <stdio.h>
#include <stdlib.h>
#include "include/bit_operation.h"
#include "include/bit_permutation.h"
#include "include/key_schedule.h"
#include "include/round_function.h"

int main() {

    uint8_t r_block[] = {
        0b01010110,
        0b01111011,
        0b11011010,
        0b11010111
    };

    uint8_t expanded_r_block[6] = {0};
    uint8_t xor_expanded_r_block[6] = {0};

    uint8_t round_key[] = {
        0b01010110,
        0b01111011,
        0b11011010,
        0b11010111,
        0b11110111,
        0b11111111
    };

    bit_permutation(r_block, 32, E_TABLE, false, true, expanded_r_block, 48);
    byte_xor(expanded_r_block, round_key, xor_expanded_r_block, 6);

    for (int i = 0; i < 32; i++) {
        if (i % 4 == 0) {
            printf("   ");
        }
        printf("%d", get_bit(r_block, i));
    }

    printf("\n ");

    for (int i = 0; i < 48; i++) {
        if (i % 6 == 0) {
            printf(" ");
        }
        printf("%d", get_bit(expanded_r_block, i));
    }
    printf("\n\n");

    for (int i = 0; i < 48; i++) {
        if (i % 6 == 0) {
            printf(" ");
        }
        printf("%d", get_bit(round_key, i));
    }
    printf("\n");

    for (int i = 0; i < 48; i++) {
        if (i % 6 == 0) {
            printf(" ");
        }
        printf("%d", get_bit(xor_expanded_r_block, i));
    }
    printf("\n\n");

    // for (int i = 0; i < S_BLOCK_COUNT; i++) {
    //     printf("s-transform=");
    //     uint8_t tr = s_transform(get_input_for_s_transform(expanded_r_block, i), i);
    //     for (int i = 0; i < 4; i++) {
    //         printf("%d", get_bit(&tr, 3 + i));
    //     }
    //     printf("\n");
    // }

    uint8_t res_block[4] = {0};
    
    round_function(r_block, round_key, res_block);

    print_byte_array_binary(res_block, 4);


    // uint8_t xor[6] = {0};

    // byte_xor(src, round_key, xor, 6);

    // for (int i = 0; i < 6; i++) {
    //     printf("%d ", xor[i]);
    // }

    return 0;
}