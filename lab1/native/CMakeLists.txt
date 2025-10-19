cmake_minimum_required(VERSION 3.10)

project(crypto_native LANGUAGES C)

include_directories(
    /Users/andreyartyushkov/Library/Java/JavaVirtualMachines/ms-17.0.16/Contents/Home/include/
    /Users/andreyartyushkov/Library/Java/JavaVirtualMachines/ms-17.0.16/Contents/Home/include/darwin
)

# Исходники библиотеки
add_library(crypto_native SHARED
    src/bit_operation.c
    src/bit_permutation.c
    src/feistel_network.c
    src/DES/key_schedule.c
    src/DES/round_function.c
    bridge/DESKeySchedule.c
    bridge/DESRoundFunction.c
    bridge/FeistelNetwork.c
)

set_target_properties(crypto_native PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY /Users/andreyartyushkov/Library/Java/Extensions/
)

target_include_directories(crypto_native PUBLIC include)

# Тесты
add_executable(bit_operation_test
    ../test/native/bit_operation_test.c
    src/bit_operation.c
    src/bit_permutation.c
    src/DES/key_schedule.c
    src/DES/round_function.c
    src/feistel_network.c
)

# Отладка
add_executable(main_runner
    main.c
    src/bit_operation.c
    src/bit_permutation.c
    src/DES/key_schedule.c
    src/DES/round_function.c
    src/feistel_network.c
)

target_include_directories(bit_operation_test PUBLIC include)

