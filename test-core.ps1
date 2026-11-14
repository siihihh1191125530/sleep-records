$ErrorActionPreference='Stop'
$root=$PSScriptRoot
$classes=Join-Path $root 'build/core-tests'
New-Item -ItemType Directory -Force -Path $classes | Out-Null
$sources=@('src/cn/local/psqirecord/Form.java','src/cn/local/psqirecord/Scales.java','src/cn/local/psqirecord/BackupFile.java','tests/CoreTest.java','tests/ScaleTest.java') | ForEach-Object {Join-Path $root $_}
& javac -encoding UTF-8 --release 8 -d $classes $sources
if ($LASTEXITCODE -ne 0) {throw 'Test compilation failed'}
foreach ($test in @('CoreTest','ScaleTest')) { & java -cp $classes $test; if ($LASTEXITCODE -ne 0) {throw "$test failed"} }
