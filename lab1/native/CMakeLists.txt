cmake_minimum_required(VERSION 3.10)

project(crypto_native LANGUAGES C)

if(NOT DEFINED ENV{JAVA_HOME})
    message(FATAL_ERROR "JAVA_HOME is not set. Please set it to your JDK path.")
endif()

set(JAVA_INCLUDE_DIR "$ENV{JAVA_HOME}/include")
if(APPLE)
    set(JAVA_PLATFORM_INCLUDE_DIR "$ENV{JAVA_HOME}/include/darwin")
elseif(WIN32)
    set(JAVA_PLATFORM_INCLUDE_DIR "$ENV{JAVA_HOME}/include/win32")
else()
    set(JAVA_PLATFORM_INCLUDE_DIR "$ENV{JAVA_HOME}/include/linux")
endif()

include_directories(
    ${JAVA_INCLUDE_DIR}
    ${JAVA_PLATFORM_INCLUDE_DIR}
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
    bridge/NativeFunctions.c
)

set_target_properties(crypto_native PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
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
