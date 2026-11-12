@echo off
setlocal EnableDelayedExpansion
pushd "%~dp0" || exit /b 1
if not exist "bin\" mkdir "bin" || goto failure
set "sources=bin\sources.txt"
>"%sources%" (
    for /r "%CD%\src" %%F in (*.java) do (
        set "file=%%F"
        echo "!file:\=/!"
    )
)
if errorlevel 1 goto failure
javac --release 17 -encoding UTF-8 -d bin @"%sources%" || goto failure
java -cp bin Main || goto failure
popd
if /i not "%~1"=="--no-pause" pause
exit /b 0

:failure
echo Build or launch failed. Check that JDK 17 or later is on PATH.
popd
if /i not "%~1"=="--no-pause" pause
exit /b 1
