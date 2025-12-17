# Smart Parking System - Build Script (PowerShell)
# Compiles and runs the project without Maven

param(
    [string]$action = "build"  # "build", "run", "test", or "clean"
)

$projectRoot = $PSScriptRoot
$srcDir = Join-Path $projectRoot "src\main\java"
$testDir = Join-Path $projectRoot "src\test\java"
$outDir = Join-Path $projectRoot "out"
$classesDir = Join-Path $outDir "classes"
$testClassesDir = Join-Path $outDir "test-classes"

Write-Host "Smart Parking System - Build Script"
Write-Host "===================================="

function Compile-Main {
    Write-Host "`n[Compiling] Main source files..."
    if (!(Test-Path $classesDir)) {
        New-Item -ItemType Directory -Path $classesDir | Out-Null
    }
    
    $javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter "*.java"
    if ($javaFiles.Count -eq 0) {
        Write-Host "  No Java files found in $srcDir"
        return $false
    }
    
    $javacCmd = @(
        'javac'
        '-d', $classesDir
        '-encoding', 'UTF-8'
    )
    
    foreach ($file in $javaFiles) {
        $javacCmd += $file.FullName
    }
    
    & $javacCmd
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Compilation failed"
        return $false
    }
    Write-Host "  OK - Compiled $($javaFiles.Count) files"
    return $true
}

function Compile-Tests {
    Write-Host "`n[Compiling] Test source files..."
    if (!(Test-Path $testClassesDir)) {
        New-Item -ItemType Directory -Path $testClassesDir | Out-Null
    }
    
    $testFiles = Get-ChildItem -Path $testDir -Recurse -Filter "*.java"
    if ($testFiles.Count -eq 0) {
        Write-Host "  No test files found"
        return $true
    }
    
    $javacCmd = @(
        'javac'
        '-d', $testClassesDir
        '-cp', $classesDir
        '-encoding', 'UTF-8'
    )
    
    foreach ($file in $testFiles) {
        $javacCmd += $file.FullName
    }
    
    & $javacCmd
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Test compilation failed"
        return $false
    }
    Write-Host "  OK - Compiled $($testFiles.Count) test files"
    return $true
}

function Run-Demo {
    Write-Host "`n[Running] Demo - Main class..."
    & java -cp $classesDir "com.example.smartparking.controller.Main"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Demo execution failed"
        return $false
    }
    return $true
}

function Run-Tests {
    Write-Host "`n[Running] Tests..."
    $testFiles = Get-ChildItem -Path $testClassesDir -Recurse -Filter "*Test.class"
    if ($testFiles.Count -eq 0) {
        Write-Host "  No test files found"
        return $true
    }
    
    foreach ($testFile in $testFiles) {
        $testClass = $testFile.FullName -replace "\.class$", "" -replace [regex]::Escape($testClassesDir), "" -replace "\\", "." -replace "^\.", ""
        Write-Host "  Running: $testClass"
        & java -cp "$testClassesDir;$classesDir" $testClass
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  ERROR: Test failed"
            return $false
        }
    }
    return $true
}

function Clean {
    Write-Host "`n[Cleaning] Output directory..."
    if (Test-Path $outDir) {
        Remove-Item -Recurse -Force $outDir
        Write-Host "  OK - Cleaned $outDir"
    } else {
        Write-Host "  (nothing to clean)"
    }
}

# Main flow
switch ($action.ToLower()) {
    "build" {
        $success = Compile-Main
        if (!$success) { exit 1 }
    }
    "test" {
        $success = Compile-Main
        if (!$success) { exit 1 }
        $success = Compile-Tests
        if (!$success) { exit 1 }
        $success = Run-Tests
        if (!$success) { exit 1 }
    }
    "run" {
        $success = Compile-Main
        if (!$success) { exit 1 }
        $success = Run-Demo
        if (!$success) { exit 1 }
    }
    "clean" {
        Clean
    }
    default {
        Write-Host "Usage: .\build.ps1 [action]"
        Write-Host "Actions:"
        Write-Host "  build  - Compile main source (default)"
        Write-Host "  run    - Compile and run demo"
        Write-Host "  test   - Compile and run tests"
        Write-Host "  clean  - Delete output directory"
        exit 1
    }
}

Write-Host "`n[Done]"
