# sc_moments_launcher_android

sc_moments_launcher_android is a Stagecast moments launcher for android clients

# Stagecast SDK Setup Guide For Android Client Projects Vol-2

To run Stagecast Moments from an android app, we first need to build the library that is published through jitpack. Then we need to add the native dependencies to the app. Suggested gradle plugin is :

```
classpath 'com.android.tools.build:gradle:3.1.4'
```

Compatibility with other gradle versions has not been tested yet.


1.Goto project level gradle and add these 2 maven repositories under allprojects


```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
        maven {
            url "$rootDir/node_modules/react-native/android"
        }
    }
}
```

2. Get Stagecast SDK from jitpack : Goto app/module level gradle that you want to use Stagecast from, and add the following dependency

dependencies {
…
implementation 'com.github.stagecast:sc_moments_launcher_android:v2.0'

}


To avoid React Native crashing on 64-bit devices, add these lines on the same file, under defaultConfig

```
defaultConfig {
…

ndk {
    abiFilters "armeabi-v7a", "x86", 'armeabi', 'arm64-v8a'
}
packagingOptions {
    exclude "lib/arm64-v8a/libgnustl_shared.so"
    exclude '/lib/mips64/**'
    exclude '/lib/arm64-v8a/**'
    exclude '/lib/x86_64/**'
}

}
```

3. Create a node_modules directory on the same level with your settings.gradle file and add the following libraries there 

```
"react-native": "0.57.7",
"react-native-camera": "^1.6.4",
"react-native-flash": "^0.2.2",
"react-native-gesture-handler": "^1.0.10",
"react-native-keep-awake": "^4.0.0",
"react-native-languages": "3.0.2",
"react-native-linear-gradient": "^2.5.2",
"react-native-mergefilterlibrary": “1.0”,
"rn-fetch-blob": "^0.10.14"
```

You can find these projects here
https://github.com/stagecast/SampleExternalAndroidAppUsingStagecast/tree/master/node_modules


4. Include the native dependencies  in node_modules to your settings.gradle file

```
include ':react-native-keep-awake'
project(':react-native-keep-awake').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-keep-awake/android')
include ':react-native-languages'
project(':react-native-languages').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-languages/android')
include ':rn-fetch-blob'
project(':rn-fetch-blob').projectDir = new File(rootProject.projectDir, 'node_modules/rn-fetch-blob/android')
include ':react-native-flash'
project(':react-native-flash').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-flash/android')
include ':react-native-camera'
project(':react-native-camera').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-camera/android')
include ':react-native-linear-gradient'
project(':react-native-linear-gradient').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-linear-gradient/android')
include ':react-native-gesture-handler'
project(':react-native-gesture-handler').projectDir = new File(rootProject.projectDir, 'node_modules/react-native-gesture-handler/android')
include ':react-native-mergefilterlibrary'
project(':react-native-mergefilterlibrary').projectDir = new File(rootProject.projectDir, 	'node_modules/react-native-mergefilterlibrary/android')
```


5. Now, compile these libraries with your app/module level gradle, add constraint-layout library if you don’t have it already

```
implementation ‘com.github.stagecast:sc_moments_launcher_android:v2.0'
implementation 'com.android.support.constraint:constraint-layout:1.1.3'

implementation "com.facebook.react:react-native:0.57.7"
implementation project(':react-native-keep-awake')
implementation project(':react-native-languages')
implementation project(':rn-fetch-blob')
implementation project(':react-native-flash')
implementation project(':react-native-camera')
implementation project(':react-native-linear-gradient')
implementation project(':react-native-gesture-handler')
implementation project(':react-native-mergefilterlibrary')
```

6. Build the project.

7. Go to the controller that you want to use Stagecast from.  And create a ReactInstanceManager

```
ReactInstanceManager reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .addPackage(new MainReactPackage())
                .addPackage(new StagecastReactPackage())
                .addPackage(new RNLanguagesPackage())
                .addPackage(new KCKeepAwakePackage())
                .addPackage(new RNFetchBlobPackage())
		     .addPackage(new ReactNativeFlashPackage())
                .addPackage(new RNCameraPackage())
                .addPackage(new LinearGradientPackage())
                .addPackage(new RNGestureHandlerPackage())
                .addPackage(new RNMergefilterlibraryPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

```
        
8. Start StagecastMomentsApi by calling

```
StagecastMomentsApi.getInstance().startReactApp(this,reactInstanceManager,"");
```
Where this : activity
"" : event id, can be empty string for now until the new release of the SDK, need to add all other props as well.

9. Now run your project. You should see the moment feed.

