#include <assert.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>

#include "../../native/include/bit_operation.h"
#include "../../native/include/bit_permutation.h"
#include "../../native/include/DES/key_schedule.h"
#include "../../native/include/DES/round_function.h"
#include "../../native/include/feistel_network.h"

static void assert_only_bit_changed(
    const uint8_t *before,
    const uint8_t *after,
    size_t len,                     
    uint8_t bit_index_changed) {

    size_t byte_changed = bit_index_changed / 8;
    uint8_t mask = (uint8_t)(1u << (7 - bit_index_changed % 8));
    for (size_t i = 0; i < len; ++i) {
        if (i == byte_changed) {
            uint8_t diff = (uint8_t)(before[i] ^ after[i]);
            assert((diff & (uint8_t)~mask) == 0 && "Unexpected bit flip in the changed byte");
        } else {
            assert(before[i] == after[i] && "Other bytes must not change");
        }
    }
}

static void test_get_on_zero() {
    uint8_t b[4] = {0};
    for (uint8_t i = 0; i < 32; ++i) {
        assert(get_bit(b, i) == 0);
    }
}

static void test_set_individual_bits() {
    uint8_t b[4] = {0,0,0,0};
    // Ставим биты 0,7,8,15,23
    set_bit(b, 0, 1);
    set_bit(b, 1, 1);   
    set_bit(b, 7, 1);   
    set_bit(b, 8, 1);   
    set_bit(b, 15, 1);  
    set_bit(b, 23, 1); 

    assert(b[0] == (uint8_t)0b11000001);
    assert(b[1] == (uint8_t)0b10000001);
    assert(b[2] == (uint8_t)0b00000001);
    assert(b[3] == (uint8_t)0b0);

    // Проверим чтение
    assert(get_bit(b, 0)  == 1);
    assert(get_bit(b, 7)  == 1);
    assert(get_bit(b, 8)  == 1);
    assert(get_bit(b, 15) == 1);
    assert(get_bit(b, 23) == 1);
    assert(get_bit(b, 1)  == 1);
    assert(get_bit(b, 14) == 0);
    assert(get_bit(b, 31) == 0);
}

static void test_clear_bits() {
    uint8_t b[4] = {0b11111111, 0b11111111, 0b11111111, 0b11111111};
    // Очистим несколько битов
    set_bit(b, 0, 0);
    set_bit(b, 7, 0);
    set_bit(b, 15, 0);
    set_bit(b, 23, 0);
    set_bit(b, 31, 0);

    assert(get_bit(b, 0)  == 0);
    assert(get_bit(b, 7)  == 0);
    assert(get_bit(b, 15) == 0);
    assert(get_bit(b, 23) == 0);
    assert(get_bit(b, 31) == 0);

    assert(b[0] == (uint8_t)0b01111110);
    assert(b[1] == (uint8_t)0b11111110);
    assert(b[2] == (uint8_t)0b11111110);
    assert(b[3] == (uint8_t)0b11111110);
}

static void test_idempotence() {
    uint8_t b[2] = {0};
    // Дважды ставим 1
    set_bit(b, 9, 1);
    uint8_t before[2]; memcpy(before, b, 2);
    set_bit(b, 9, 1);
    assert(memcmp(before, b, 2) == 0);

    // Дважды ставим 0
    set_bit(b, 9, 0);
    memcpy(before, b, 2);
    set_bit(b, 9, 0);
    assert(memcmp(before, b, 2) == 0);
}

static void test_only_target_bit_changes() {
    uint8_t b[3] = {0b10101010, 0b01010101, 0b11110000};
    uint8_t before[3];
    
    memcpy(before, b, 3);

    set_bit(b, 10, 1);
    assert_only_bit_changed(before, b, 3, 10);

    memcpy(before, b, 3);
    set_bit(b, 10, 0);
    assert_only_bit_changed(before, b, 3, 10);
}

static void test_highest_index_in_array() {
    // 4 байта => индексы 0..31
    uint8_t b[4] = {0};
    set_bit(b, 31, 1); // последний бит
    assert(get_bit(b, 31) == 1);
    assert(b[3] == (uint8_t)0b00000001);
    // Снимем обратно
    set_bit(b, 31, 0);
    assert(get_bit(b, 31) == 0);
    assert(b[3] == (uint8_t)0b0);
}

static void test_cross_byte_sequences() {
    uint8_t b[2] = {0};
    // Поставим цепочку битов, переходящую через границу байтов: 6,7,8,9
    set_bit(b, 6, 1);
    set_bit(b, 7, 1);
    set_bit(b, 8, 1);
    set_bit(b, 9, 1);
    // Ожидаем: byte0 = ..11 000000 => 11000000b = 0xC0
    //          byte1 = 0000 0011b = 0x03
    assert(b[0] == (uint8_t)0b00000011);
    assert(b[1] == (uint8_t)0b11000000);

    // Проверим чтение
    assert(get_bit(b, 6) == 1);
    assert(get_bit(b, 7) == 1);
    assert(get_bit(b, 8) == 1);
    assert(get_bit(b, 9) == 1);
    assert(get_bit(b, 5) == 0);
    assert(get_bit(b, 10) == 0);
}

void test_case(
    const char* name,
    const uint8_t* input,
    size_t input_len,
    const size_t* permutation,
    bool is_reverse_order,
    bool bit_index_starts_at_1,
    size_t output_len,
    const uint8_t* expected
) {
    uint8_t output[8] = {0}; // до 64 бит
    bit_permutation(input,
         input_len,
          permutation,
           is_reverse_order,
            bit_index_starts_at_1,
             output,
              output_len);

    printf("Test: %s\n", name);
    printf("Expected: ");
    print_byte_array_binary(expected, (output_len / 8) + (output_len % 8 != 0));
    printf("Got     : ");
    print_byte_array_binary(output, (output_len / 8) + (output_len % 8 != 0));
    printf("Result  : %s\n", memcmp(output, expected, (output_len + 7) / 8) == 0 ? "✅ OK" : "❌ FAIL");
    printf("-----------------------------------------------------\n");
}

void test_key_schedule() {
    uint8_t key[KEY_BYTE_SIZE] = {
        0b00010011,
        0b00110100,
        0b01010111,
        0b01111001,
        0b10011011,
        0b10111100,
        0b11011111,
        0b11110001
    };

    /*
    
    https://ritul-patidar.medium.com/key-expansion-function-and-key-schedule-of-des-data-encryption-standard-algorithm-1bfc7476157
    
    */

    uint8_t expected_key[NUM_ROUNDS][ROUND_KEY_SIZE] = {
        {0b00011011, 0b00000010, 0b11101111, 0b11111100, 0b01110000, 0b01110010},
        {0b01111001, 0b10101110, 0b11011001, 0b11011011, 0b11001001, 0b11100101},
        {0b01010101, 0b11111100, 0b10001010, 0b01000010, 0b11001111, 0b10011001},
        {0b01110010, 0b10101101, 0b11010110, 0b11011011, 0b00110101, 0b00011101},
        {0b01111100, 0b11101100, 0b00000111, 0b11101011, 0b01010011, 0b10101000},
        {0b01100011, 0b10100101, 0b00111110, 0b01010000, 0b01111011, 0b00101111},
        {0b11101100, 0b10000100, 0b10110111, 0b11110110, 0b00011000, 0b10111100},
        {0b11110111, 0b10001010, 0b00111010, 0b11000001, 0b00111011, 0b11111011},
        {0b11100000, 0b11011011, 0b11101011, 0b11101101, 0b11100111, 0b10000001},
        {0b10110001, 0b11110011, 0b01000111, 0b10111010, 0b01000110, 0b01001111},
        {0b00100001, 0b01011111, 0b11010011, 0b11011110, 0b11010011, 0b10000110},
        {0b01110101, 0b01110001, 0b11110101, 0b10010100, 0b01100111, 0b11101001},
        {0b10010111, 0b11000101, 0b11010001, 0b11111010, 0b10111010, 0b01000001},
        {0b01011111, 0b01000011, 0b10110111, 0b11110010, 0b11100111, 0b00111010},
        {0b10111111, 0b10010001, 0b10001101, 0b00111101, 0b00111111, 0b00001010},
        {0b11001011, 0b00111101, 0b10001011, 0b00001110, 0b00010111, 0b11110101}
    };

    uint8_t result_key[NUM_ROUNDS][ROUND_KEY_SIZE] = {0};

    generate_round_keys_des(key, result_key);

    printf("KEY SCHEDULE : %s\n", memcmp(expected_key, result_key, NUM_ROUNDS * ROUND_KEY_SIZE) == 0 ? "✅ OK" : "❌ FAIL");
};

void test_round_function() {
    uint8_t r_block[] = {
        0b01010110,
        0b01111011,
        0b11011010,
        0b11010111
    };
    uint8_t expanded_r_block[6] = {0};
    uint8_t expected_expanded_round_block[6] = {
        0b10101010,
        0b11000011,
        0b11110111,
        0b11101111,
        0b01010110,
        0b10101110
    };
    bit_permutation(r_block, 32, E_TABLE, false, true, expanded_r_block, 48);
    printf("EXPANDED FUNCTION : %s\n", memcmp(expanded_r_block, expected_expanded_round_block, 6) == 0 ? "✅ OK" : "❌ FAIL");

    //==========================================================================
    uint8_t expected_xor[6] = {0};
    uint8_t result_xor[6] = {0};
    byte_xor(expanded_r_block, expanded_r_block, result_xor, 6);
    printf("XOR TEST : %s\n", memcmp(expected_xor, result_xor, 6) == 0 ? "✅ OK" : "❌ FAIL");
    //==========================================================================

    uint8_t expected_cypher_blockp[] = {
        0b11010010,
        0b11011101,
        0b01100010,
        0b10111100
    };

    uint8_t round_key[] = {
        0b01010110,
        0b01111011,
        0b11011010,
        0b11010111,
        0b11110111,
        0b11111111
    };

    uint8_t result_cypher_block[4] = {0};

    round_function_des(r_block, round_key, result_cypher_block);

    printf("ROUND FUNCTION TEST : %s\n", memcmp(expected_cypher_blockp, result_cypher_block, 4) == 0 ? "✅ OK" : "❌ FAIL");
}

static void test_single_round_matches_manual() {
    const size_t block_len = 8; // 64 бита: 32 бита слева, 32 справа
    const size_t rounds = 2;

    // детерминированные данные
    uint8_t input[8] = { 0x6b,0xc1,0xbe,0xe2, 0x2e,0x40,0x9f,0x96 }; // просто фиксированный пример  
    uint8_t **encrypt_keys = (uint8_t **)malloc(sizeof(uint8_t *) * rounds);
    encrypt_keys[0] = (uint8_t *)malloc(sizeof(uint8_t) * 6);
    encrypt_keys[1] = (uint8_t *)malloc(sizeof(uint8_t) * 6);

    encrypt_keys[0][0] = 0x13;
    encrypt_keys[0][1] = 0x34;
    encrypt_keys[0][2] = 0x57;
    encrypt_keys[0][3] = 0x79;
    encrypt_keys[0][4] = 0x9b;
    encrypt_keys[0][5] = 0xbc;

    encrypt_keys[1][0] = 0x23;
    encrypt_keys[1][1] = 0x54;
    encrypt_keys[1][2] = 0x77;
    encrypt_keys[1][3] = 0x29;
    encrypt_keys[1][4] = 0xab;
    encrypt_keys[1][5] = 0xfc;

    uint8_t **decrypt_keys = (uint8_t **)malloc(sizeof(uint8_t *) * rounds);
    decrypt_keys[0] = (uint8_t *)malloc(sizeof(uint8_t) * 6);
    decrypt_keys[1] = (uint8_t *)malloc(sizeof(uint8_t) * 6);

    decrypt_keys[0][0] = 0x23;
    decrypt_keys[0][1] = 0x54;
    decrypt_keys[0][2] = 0x77;
    decrypt_keys[0][3] = 0x29;
    decrypt_keys[0][4] = 0xab;
    decrypt_keys[0][5] = 0xfc;

    decrypt_keys[1][0] = 0x13;
    decrypt_keys[1][1] = 0x34;
    decrypt_keys[1][2] = 0x57;
    decrypt_keys[1][3] = 0x79;
    decrypt_keys[1][4] = 0x9b;
    decrypt_keys[1][5] = 0xbc;


    uint8_t *output = (uint8_t *)malloc(sizeof(uint8_t) * 8);
    uint8_t *output1 = (uint8_t *)malloc(sizeof(uint8_t) * 8);
    print_byte_array_binary(input, 8);
    feistel_network_apply(input, output, encrypt_keys, rounds, block_len, round_function_des);
    print_byte_array_binary(output, 8);
    feistel_network_apply(output, output1, decrypt_keys, rounds, block_len, round_function_des);
    print_byte_array_binary(output1, 8);
    // // ручной шаг для проверки
    // const size_t half = block_len / 2;
    // uint8_t L0[4], R0[4];
    // memcpy(L0, input,       half);
    // memcpy(R0, input + half, half);

    // uint8_t F[4] = {0};
    // round_function_des(R0, key_flat, F);

    // uint8_t newR[4] = {0};
    // for (size_t i = 0; i < 4; i++) newR[i] = L0[i] ^ F[i];

    // // после цикла твоя функция делает финальную «перестановку половинок»: out = R1 || L1
    // // где L1 = R0, R1 = newR
    // uint8_t expected[8] = {0};
    // memcpy(expected, newR, 4); // R1
    // memcpy(expected + 4, R0, 4);  // L1

    // assert(memcmp(output, expected, 8) == 0 && "single round result must match manual Feistel step");

    // printf("test_single_round_matches_manual: OK\n");
}


int main(void) {
    // test_get_on_zero();
    // test_set_individual_bits();
    // test_clear_bits();
    // test_idempotence();
    // test_only_target_bit_changes();
    // test_highest_index_in_array();
    // test_cross_byte_sequences(); 
    // test_single_round_matches_manual();

    // uint8_t input1[1] = {0b10101010};
    // size_t perm1[8] = {0,1,2,3,4,5,6,7};
    // uint8_t expected1[1] = {0b10101010};
    // test_case("Identity permutation", input1, 8, perm1, false, false, 8, expected1);

    // size_t perm2[8] = {7,6,5,4,3,2,1,0};
    // uint8_t expected2[1] = {0b10101010};
    // test_case("Reverse order", input1, 8, perm2, true, false, 8, expected2);

    // size_t perm3[8] = {1,2,3,4,5,6,7,8};
    // uint8_t expected3[1] = {0b10101010};
    // test_case("1-based indexing", input1, 8, perm3, false, true, 8, expected3);

    // size_t perm4[8] = {8,7,6,5,4,3,2,1};
    // uint8_t expected4[1] = {0b10101010};
    // test_case("Reverse + 1-based", input1, 8, perm4, true, true, 8, expected4);

    // size_t perm5[4] = {0,1,2,3};
    // const uint8_t expected5[1] = {0b10100000};
    // test_case("Partial bits", input1, 8, perm5, false, false, 4, expected5);

    // test_key_schedule();
    // test_round_function();

    size_t perm[] = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    uint8_t input[] = {34, 21, 54, 78, 14, 64, 0, 1};

    uint8_t output[0] = {0};

    bit_permutation(input, 64, perm, false, true, output, 64);

    for (int i = 0; i < 8; i++) {
        printf("%4d ", output[i]);
    }


    return 0;
}

