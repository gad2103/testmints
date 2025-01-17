plugins {
    id("com.zegreatrob.testmints.plugins.versioning")
    id("com.zegreatrob.testmints.plugins.publish")
    id("com.zegreatrob.testmints.plugins.platforms")
}

kotlin {
    js {
        val compilation = compilations["test"]
        binaries.executable(compilation)

        compilation.packageJson {
            customField("mocha", mapOf("require" to "./kotlin/mint-logs.mjs"))
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":standard"))
                implementation(project(":report"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                implementation("io.github.microutils:kotlin-logging")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("org.junit.jupiter:junit-jupiter-engine")
            }
        }

        val nativeCommonMain by creating {
            dependsOn(commonMain)
        }

        val macosX64Main by getting { dependsOn(nativeCommonMain) }

        val linuxX64Main by getting { dependsOn(nativeCommonMain) }

        val iosX64Main by getting { dependsOn(nativeCommonMain) }

        val mingwX64Main by getting { dependsOn(nativeCommonMain) }

        val jsMain by getting {
            dependencies {
                dependsOn(commonMain)
            }
        }
    }
}

tasks {
    jsTestProcessResources {
        dependsOn(jsProcessResources)
        from(jsProcessResources.map { it.outputs.files })
    }
    jsNodeTest {
        dependsOn(jsTestTestProductionExecutableCompileSync)
    }

    jvmTest {
        useJUnitPlatform()
        systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    }
}

dependencies {
    jvmTestImplementation("org.slf4j:slf4j-simple")
}