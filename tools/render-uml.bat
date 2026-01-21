@echo off
REM Render UML helper: uses plantuml.jar (place it in repo root or set PLANTUML_JAR env var)
SETLOCAL ENABLEDELAYEDEXPANSION
if exist "..\plantuml.jar" (
  set PLANTUML_JAR="..\plantuml.jar"
) else if defined PLANTUML_JAR (
  rem use environment variable
) else (
  echo ERROR: plantuml.jar not found. Download from https://plantuml.com/download and place it in the repo root or set PLANTUML_JAR env var.
  pause
  exit /b 1
)














pause)  echo Rendering finished; check PlantUML output for errors.) else (  echo Rendered: ..\uml\project_diagram.pngif exist "..\uml\project_diagram.png" (java -jar %PLANTUML_JAR% -tpng "..\uml\project_diagram.puml"
necho Rendering UML -> PNG...)  exit /b 1  pause  echo ERROR: UML file not found: ..\uml\project_diagram.pumlnif not exist "..\uml\project_diagram.puml" (