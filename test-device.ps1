param([string]$Device='emulator-5554',[string]$Mode='storage')
$ErrorActionPreference='Stop'
$root=$PSScriptRoot
$sdk=if ($env:ANDROID_HOME) {$env:ANDROID_HOME} elseif ($env:ANDROID_SDK_ROOT) {$env:ANDROID_SDK_ROOT} else {Join-Path (Split-Path $PSScriptRoot) 'android-sdk'}
& (Join-Path $PSScriptRoot 'setup-tools.ps1')
$bt=Join-Path $sdk 'build-tools\34.0.0'
$jar=Join-Path $sdk 'platforms\android-34\android.jar'
$build=Join-Path $root 'build\instrumentation'
New-Item -ItemType Directory -Force -Path $build,(Join-Path $build 'classes'),(Join-Path $build 'dex') | Out-Null
function Check-Exit {if ($LASTEXITCODE -ne 0){throw 'Test build failed'}}
& (Join-Path $bt 'aapt2.exe') link -I $jar --manifest (Join-Path $root 'tests\AndroidManifest.xml') -o (Join-Path $build 'unsigned.apk')
Check-Exit
& javac -encoding UTF-8 --release 8 -classpath ($jar+';'+(Join-Path $root 'build\classes')) -d (Join-Path $build 'classes') (Join-Path $root 'tests\DeviceTests.java')
Check-Exit
& jar cf (Join-Path $build 'classes.jar') -C (Join-Path $build 'classes') .
& java -cp (Join-Path $root 'r8-8.3.37.jar') com.android.tools.r8.D8 --min-api 26 --lib $jar --classpath (Join-Path $root 'build\classes.jar') --output (Join-Path $build 'dex') (Join-Path $build 'classes.jar')
Check-Exit
& jar uf (Join-Path $build 'unsigned.apk') -C (Join-Path $build 'dex') classes.dex
& (Join-Path $bt 'zipalign.exe') -f -p 4 (Join-Path $build 'unsigned.apk') (Join-Path $build 'aligned.apk')
& (Join-Path $bt 'apksigner.bat') sign --ks $(if ($env:SLEEP_RECORDS_KEYSTORE) {$env:SLEEP_RECORDS_KEYSTORE} else {Join-Path $root 'private\release.p12'}) --ks-pass ('file:'+$(if ($env:SLEEP_RECORDS_PASSWORD_FILE) {$env:SLEEP_RECORDS_PASSWORD_FILE} else {Join-Path $root 'private\signing-password.txt'})) --out (Join-Path $build 'tests.apk') (Join-Path $build 'aligned.apk')
Check-Exit
& (Join-Path $sdk 'platform-tools\adb.exe') -s $Device install -r (Join-Path $build 'tests.apk')
Check-Exit
& (Join-Path $sdk 'platform-tools\adb.exe') -s $Device shell am instrument -w -e mode $Mode cn.local.psqirecord.tests/cn.local.psqirecord.tests.DeviceTests
