plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)  // ⬅️ AJOUTÉ : Hilt
    alias(libs.plugins.ksp)           // ⬅️ AJOUTÉ : KSP
}

android {
    namespace = "com.bizetj.goldeneratracker"
    compileSdk = 34  // ⬅️ MODIFIÉ : Simplifié (36 n'existe pas encore officiellement)

    defaultConfig {
        applicationId = "com.bizetj.goldeneratracker"
        minSdk = 26
        targetSdk = 34  // ⬅️ MODIFIÉ : 34 = Android 14 (stable)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ⬅️ AJOUTÉ : Nécessaire pour Compose
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    // ⬅️ AJOUTÉ : Configuration Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    // ⬅️ AJOUTÉ : Évite les conflits de META-INF
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ═══════════════════════════════════════════════════════
    // COMPOSE (Interface utilisateur moderne)
    // ═══════════════════════════════════════════════════════
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // ═══════════════════════════════════════════════════════
    // NAVIGATION (Gérer les 3 onglets : Liste, Création, Stats)
    // ═══════════════════════════════════════════════════════
    implementation(libs.androidx.navigation.compose)

    // ═══════════════════════════════════════════════════════
    // HILT (Injection de dépendances)
    // Pourquoi ? Fournit automatiquement Database, DAOs partout
    // ═══════════════════════════════════════════════════════
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)  // Génère le code Hilt
    implementation(libs.androidx.hilt.navigation.compose)

    // ═══════════════════════════════════════════════════════
    // ROOM (Base de données locale SQLite)
    // Pourquoi ? Sauvegarder séances, exercices, historique
    // ═══════════════════════════════════════════════════════
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)  // Support Coroutines
    ksp(libs.androidx.room.compiler)  // Génère les DAOs

    // ═══════════════════════════════════════════════════════
    // VIEWMODEL (Architecture MVVM)
    // Pourquoi ? Séparer la logique de l'interface
    // ═══════════════════════════════════════════════════════
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // ═══════════════════════════════════════════════════════
    // COROUTINES (Programmation asynchrone)
    // Pourquoi ? Lire/écrire en BDD sans freezer l'interface
    // ═══════════════════════════════════════════════════════
    implementation(libs.kotlinx.coroutines.android)

    // ═══════════════════════════════════════════════════════
    // VICO (Graphiques pour l'onglet Statistiques)
    // Pourquoi ? Afficher courbes de progression, volume, etc.
    // ═══════════════════════════════════════════════════════
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    // ═══════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}