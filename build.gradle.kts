plugins {
    id("java")
}

group = "site.ichocomilk"
version = "1.0.0"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://maven.elmakers.com/repository")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.36")
        compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
        compileOnly("it.unimi.dsi:fastutil:8.5.15")

        annotationProcessor("org.projectlombok:lombok:1.18.36")

        testCompileOnly("org.projectlombok:lombok:1.18.36")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    apply<JavaPlugin>()

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}