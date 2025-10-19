plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  id("org.jlleitschuh.gradle.ktlint")
  id("io.gitlab.arturbosch.detekt")
}

android {
  namespace = "com.example.androidmodularshowcase"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.androidmodularshowcase"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }
  }

  lint {
    abortOnError = true
    warningsAsErrors = false
    xmlReport = true
    htmlReport = true
    xmlOutput = file("${project.buildDir}/reports/lint/lint-results.xml")
    htmlOutput = file("${project.buildDir}/reports/lint/lint-results.html")

    // 禁用一些不必要的检查
    disable += setOf(
      "TypographyFractions",
      "TypographyQuotes",
      "JCenterRepository",
      "GradleDependency",
    )
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
}

dependencies {

  // Compose 基础
  implementation(platform("androidx.compose:compose-bom:2024.06.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.ui:ui-tooling-preview")

  // 预览调试
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  // Lifecycle / ViewModel / Navigation 支持
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
  implementation("androidx.activity:activity-compose:1.9.0")
  implementation("androidx.navigation:navigation-compose:2.8.0")

  // 其他默认库
//    implementation(libs.androidx.core.ktx
  implementation("androidx.core:core-ktx:1.13.1")
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}

// ktlint 配置 - 使用兼容版本
ktlint {
  version.set("0.50.0")
  android.set(true)
  ignoreFailures.set(false)
  outputToConsole.set(true)
  coloredOutput.set(true)

  reporters {
    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
  }

  filter {
    exclude("**/generated/**")
    exclude("**/build/**")
    exclude("**/test/**")
    exclude("**/androidTest/**")
    exclude("**/resources/**")
    include("**/kotlin/**")
  }
}
// detekt 配置
detekt {
  toolVersion = "1.23.7" // 使用最新版本
  config = files("${rootProject.projectDir}/config/detekt.yml")
  buildUponDefaultConfig = true
  allRules = false
  autoCorrect = true
  baseline = file("${rootProject.projectDir}/config/detekt-baseline.yml")

  reports {
    html {
      required = true
      outputLocation = file("${project.buildDir}/reports/detekt/detekt.html")
    }
    xml {
      required = true
      outputLocation = file("${project.buildDir}/reports/detekt/detekt.xml")
    }
    txt {
      required = true
      outputLocation = file("${project.buildDir}/reports/detekt/detekt.txt")
    }
  }
}
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  reports {
    html.required.set(true)
    xml.required.set(true)
    txt.required.set(true)
  }
}


// 代码质量检查聚合任务
tasks.register("codeQualityCheck") {
  group = "verification"
  description = "Runs all code quality checks"
  dependsOn(
    "lint",
    "ktlintCheck",
    "detekt",
  )
}

tasks.register("codeQualityFix") {
  group = "verification"
  description = "Fixes auto-correctable code quality issues"
  dependsOn(
    "ktlintFormat",
    "detekt",
  )
}
