@echo off
setlocal enabledelayedexpansion

:: Cartella da analizzare (puoi cambiare "." con un altro percorso)
set "folder=."

:: File temporaneo per salvare le estensioni
set "tempfile=extlist.txt"
if exist %tempfile% del %tempfile%

:: Scansiona tutti i file e raccoglie le estensioni
for /R "%folder%" %%F in (*) do (
    set "ext=%%~xF"
    if defined ext (
        set "ext=!ext:~1!"  :: rimuove il punto iniziale
        if not "!ext!"=="" (
            findstr /I /X /C:"!ext!" %tempfile% >nul || echo !ext!>>%tempfile%
        )
    )
)

:: Mostra il risultato
echo Tipi di file trovati:
type %tempfile%

pause
