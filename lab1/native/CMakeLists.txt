cmake_minimum_required(VERSION 3.10)

project(crypto_native LANGUAGES C)

# Исходники библиотеки
add_library(crypto_native SHARED
    src/bit_operation.c
    src/bit_permutation.c
    src/key_schedule.c
    src/round_function.c
)

target_include_directories(crypto_native PUBLIC include)

# Тесты
add_executable(bit_operation_test
    ../test/native/bit_operation_test.c
    src/bit_operation.c
    src/bit_permutation.c
    src/key_schedule.c
    src/round_function.c
)

# Отладка
add_executable(main_runner
    main.c
    src/bit_operation.c
    src/bit_permutation.c
    src/key_schedule.c
    src/round_function.c
)

target_include_directories(bit_operation_test PUBLIC include)