plugins {
	id("org.springframework.boot") version "2.5.0" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
	id("org.asciidoctor.convert") version "1.5.12" apply false
	kotlin("jvm") version "1.5.10" apply false
	kotlin("plugin.spring") version "1.5.10" apply false
	kotlin("plugin.jpa") version "1.5.10" apply false
}

allprojects {
	group = "com"
	version = "0.0.1-SNAPSHOT"
}

subprojects {
	repositories {
		mavenCentral()
	}
}