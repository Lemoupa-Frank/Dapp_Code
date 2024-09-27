import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.8.21"
}

group = "voting.dapp"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.hyperledger.fabric:fabric-gateway:1.4.0")
	implementation("com.google.code.gson:gson:2.8.9")
	implementation ("com.owlike:genson:1.5")
	compileOnly ("io.grpc:grpc-api:1.59.0")
	runtimeOnly ("io.grpc:grpc-netty-shaded:1.59.0")
	implementation ("com.google.code.gson:gson:2.10.1")
	implementation ("org.bouncycastle:bcprov-jdk15on:1.70")
	implementation ("org.bouncycastle:bcpkix-jdk15on:1.70")
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation(kotlin("stdlib-jdk8"))
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.8"
}