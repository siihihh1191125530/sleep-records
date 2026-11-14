$ErrorActionPreference='Stop'
$toolJar=Join-Path $PSScriptRoot 'r8-8.3.37.jar'
$expected='59753E70A74F918389CC87F1B7D66B5C0862932559167425708DED159E3DE439'
if (-not (Test-Path -LiteralPath $toolJar)) {
 Invoke-WebRequest -Uri 'https://dl.google.com/dl/android/maven2/com/android/tools/r8/8.3.37/r8-8.3.37.jar' -OutFile $toolJar
}
if ((Get-FileHash -LiteralPath $toolJar -Algorithm SHA256).Hash -ne $expected) {throw 'R8 checksum mismatch; do not use this download'}

