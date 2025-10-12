#pragma once

#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h> 
#include <stdio.h>  


uint8_t get_bit(const uint8_t* block, size_t index);
void set_bit(uint8_t* block, size_t index, uint8_t bit);

void byte_xor(uint8_t *first, uint8_t *second, uint8_t *result, size_t bytes_count);
void print_byte_array_binary(const uint8_t* data, size_t len);
void print_byte_binary(uint8_t byte);
