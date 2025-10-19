plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    java
}

group = "com.andrey"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("buildNative") {
    doLast {
        exec {
            commandLine("cmake", "--build", "native/build", "--target", "crypto_native")
        }
    }
}

tasks.named("classes")

tasks.register<JavaExec>("runMyClass") {
    mainClass.set("project.algorythm.des.DESCypher")
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.named("runMyClass") {
    dependsOn("buildNative")
    dependsOn("classes")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("-h", "$projectDir/native/bridge"))
}
