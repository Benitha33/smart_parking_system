@echo off
REM Smart Parking System - Build Script (Windows Batch)
REM Compiles and runs the project without Maven

setlocal enabledelayedexpansion
set action=%1
if "%action%"=="" set action=build

set projectRoot=%~dp0
set srcDir=%projectRoot%src\main\java
set testDir=%projectRoot%src\test\java
set outDir=%projectRoot%out
set classesDir=%outDir%\classes
set testClassesDir=%outDir%\test-classes

echo Smart Parking System - Build Script
echo ====================================

if "%action%"=="build" (
    echo.
    echo [Compiling] Main source files...
    if not exist "%classesDir%" mkdir "%classesDir%"
    javac -d "%classesDir%" -encoding UTF-8 "%srcDir%\com\example\smartparking\model\*.java" "%srcDir%\com\example\smartparking\service\*.java" "%srcDir%\com\example\smartparking\controller\*.java"
    if !errorlevel! neq 0 (
        echo ERROR: Compilation failed
        exit /b 1
    )
    echo OK - Compilation successful
    goto done
)

if "%action%"=="run" (
    echo.
    echo [Compiling] Main source files...
    if not exist "%classesDir%" mkdir "%classesDir%"
    javac -d "%classesDir%" -encoding UTF-8 "%srcDir%\com\example\smartparking\model\*.java" "%srcDir%\com\example\smartparking\service\*.java" "%srcDir%\com\example\smartparking\controller\*.java"
    if !errorlevel! neq 0 (
        echo ERROR: Compilation failed
        exit /b 1
    )
    echo OK - Compiled successfully
    echo.
    echo [Running] Demo - Main class...
    java -cp "%classesDir%" com.example.smartparking.controller.Main
    if !errorlevel! neq 0 (
        echo ERROR: Execution failed
        exit /b 1
    )
    goto done
)

if "%action%"=="test" (
    echo.
    echo [Compiling] Main source files...
    if not exist "%classesDir%" mkdir "%classesDir%"
    javac -d "%classesDir%" -encoding UTF-8 "%srcDir%\com\example\smartparking\model\*.java" "%srcDir%\com\example\smartparking\service\*.java" "%srcDir%\com\example\smartparking\controller\*.java"
    if !errorlevel! neq 0 (
        echo ERROR: Compilation failed
        exit /b 1
    )
    echo OK - Compiled main files
    echo.
    echo [Compiling] Test files...
    if not exist "%testClassesDir%" mkdir "%testClassesDir%"
    javac -d "%testClassesDir%" -cp "%classesDir%" -encoding UTF-8 "%testDir%\com\example\smartparking\service\*.java"
    if !errorlevel! neq 0 (
        echo ERROR: Test compilation failed
        exit /b 1
    )
    echo OK - Compiled test files
    echo.
    echo [Running] Tests...
    java -cp "%testClassesDir%;%classesDir%" com.example.smartparking.service.ParkingServiceTest
    if !errorlevel! neq 0 (
        echo ERROR: Tests failed
        exit /b 1
    )
    goto done
)

if "%action%"=="clean" (
    echo.
    echo [Cleaning] Output directory...
    if exist "%outDir%" (
        rmdir /s /q "%outDir%"
        echo OK - Cleaned %outDir%
    ) else (
        echo (nothing to clean)
    )
    goto done
)

echo Usage: build.bat [action]
echo Actions:
echo   build  - Compile main source (default)
echo   run    - Compile and run demo
echo   test   - Compile and run tests
echo   clean  - Delete output directory
exit /b 1

:done
echo.
echo [Done]
