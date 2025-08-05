// Java 프로젝트에서도 gradle - kotlin으로 진행
// kotlin으로 되어 있어서 ide에서 빌드스크립트를 제어하기 좋음

plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.spotbugs") version "6.2.1"
}

group = "tobyspring"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val mockitoAgent: Configuration = configurations.create("mockitoAgent")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.security:spring-security-core")
	compileOnly("org.projectlombok:lombok")

	// developmentOnly: 패키징을 하지 않은 로컬 환경에서만 동작
	// jar 파일로 패키징할 때는 동작하지 않음
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")

	annotationProcessor("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	testCompileOnly("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.junit-pioneer:junit-pioneer:2.3.0")
	testImplementation("org.mockito:mockito-core:5.18.0")
	testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
	mockitoAgent("org.mockito:mockito-core:5.18.0") { isTransitive = false }
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

spotbugs {
	excludeFilter.set(file("${projectDir}/spotbugs-exclude-filter.xml"))
}
