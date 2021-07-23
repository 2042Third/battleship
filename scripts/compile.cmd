rem @echo off
cd ../
set CDIR=%cd%
set JDIR=%CDIR%\src\main\java
set JPRG=src.main.java.
set JBACK=%cd%\scripts
cd %JDIR%

jar cmf manifest.txt hw3.jar *.class *.jpg *.png
cd %JBACK%