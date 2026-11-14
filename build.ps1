$ErrorActionPreference='Stop'
$projectRoot=$PSScriptRoot
$sdk=if ($env:ANDROID_HOME) {$env:ANDROID_HOME} elseif ($env:ANDROID_SDK_ROOT) {$env:ANDROID_SDK_ROOT} else {Join-Path (Split-Path $PSScriptRoot) 'android-sdk'}
& (Join-Path $PSScriptRoot 'setup-tools.ps1')
$bt=Join-Path $sdk 'build-tools\34.0.0'
$androidJar=Join-Path $sdk 'platforms\android-34\android.jar'
$build=Join-Path $projectRoot 'build'
New-Item -ItemType Directory -Force -Path $build,(Join-Path $build 'classes'),(Join-Path $build 'dex') | Out-Null
function Check-Exit { if ($LASTEXITCODE -ne 0) { throw "Build command failed: $LASTEXITCODE" } }
& (Join-Path $bt 'aapt2.exe') compile --dir (Join-Path $projectRoot 'res') -o (Join-Path $build 'resources.zip')
Check-Exit
& (Join-Path $bt 'aapt2.exe') link -I $androidJar --manifest (Join-Path $projectRoot 'AndroidManifest.xml') -o (Join-Path $build 'unsigned.apk') (Join-Path $build 'resources.zip') --auto-add-overlay
Check-Exit
$sources=Get-ChildItem -LiteralPath (Join-Path $projectRoot 'src') -Recurse -Filter '*.java' | ForEach-Object {$_.FullName}
& javac -encoding UTF-8 --release 8 -classpath $androidJar -d (Join-Path $build 'classes') $sources
Check-Exit
& jar cf (Join-Path $build 'classes.jar') -C (Join-Path $build 'classes') .
Check-Exit
& java -cp (Join-Path $projectRoot 'r8-8.3.37.jar') com.android.tools.r8.D8 --min-api 26 --lib $androidJar --output (Join-Path $build 'dex') (Join-Path $build 'classes.jar')
Check-Exit
& jar uf (Join-Path $build 'unsigned.apk') -C (Join-Path $build 'dex') classes.dex
Check-Exit
& (Join-Path $bt 'zipalign.exe') -f -p 4 (Join-Path $build 'unsigned.apk') (Join-Path $build 'aligned.apk')
Check-Exit
$key=if ($env:SLEEP_RECORDS_KEYSTORE) {$env:SLEEP_RECORDS_KEYSTORE} else {Join-Path $projectRoot 'private\release.p12'}
New-Item -ItemType Directory -Force -Path (Split-Path $key) | Out-Null
$passFile=if ($env:SLEEP_RECORDS_PASSWORD_FILE) {$env:SLEEP_RECORDS_PASSWORD_FILE} else {Join-Path $projectRoot 'private\signing-password.txt'}
if (-not (Test-Path -LiteralPath $key)) {
 if ($env:SLEEP_RECORDS_KEYSTORE) {throw 'Configured signing key is missing; refusing to create a replacement'}
 $password=[Guid]::NewGuid().ToString('N')+[Guid]::NewGuid().ToString('N')
 [IO.File]::WriteAllText($passFile,$password)
 & keytool -genkeypair -keystore $key -storepass:file $passFile -keypass:file $passFile -alias psqi -keyalg RSA -keysize 3072 -validity 10000 -dname 'CN=Sleep Records Personal, O=Personal Tools, C=CN' -storetype PKCS12
 Check-Exit
}
& (Join-Path $bt 'apksigner.bat') sign --ks $key --ks-pass ('file:'+$passFile) --out (Join-Path $build 'sleep-records.apk') (Join-Path $build 'aligned.apk')
Check-Exit
& (Join-Path $bt 'apksigner.bat') verify --verbose (Join-Path $build 'sleep-records.apk')
Check-Exit
Write-Output ('APK: '+(Join-Path $build 'sleep-records.apk'))
